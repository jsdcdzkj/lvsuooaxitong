package com.jsdc.lsoa.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.core.common.utils.DateUtil;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.enums.InvoiceEnums;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.AgreementCustomerMapper;
import com.jsdc.lsoa.mapper.CaseInfoMapper;
import com.jsdc.lsoa.mapper.SysUserMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.AgreementCustomerVO;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class AgreementCustomerService extends BaseService<AgreementCustomer> {


    @Autowired
    private AgreementCustomerMapper agreementCustomerMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private ProcessApproveRecordService approveRecordService;

    @Autowired
    private InvoiceInfoService invoiceInfoService;

    @Autowired
    private CustomerLawyersService customerLawyersService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    private DecimalFormat decimalFormat = new DecimalFormat("'A'0000");

    public Page<AgreementCustomerVO> getPageList(AgreementCustomerVO vo) {
        SysUser sysUser = userService.getUser();

        List<SysRole> roleList = sysUser.getRoleList();
        List<String> collect = roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

        if (!collect.contains(CommonConstants.ADMIN) && !collect.contains(CommonConstants.GWKHGLY)) {
            vo.setUserId(sysUser.getId());

        }
        Page<AgreementCustomerVO> page = agreementCustomerMapper.getPage(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            for (AgreementCustomerVO item : page.getRecords()) {
                List<Integer> ids = Arrays.stream(item.getLawyerStr().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                item.setLawyers(ids);
                List<SysUser> users = userMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getIsDel, 0).in(SysUser::getId, ids));
                String names = users.stream().map(SysUser::getRealName).collect(Collectors.joining(","));
                item.setLawyerNames(names);

                item.setInvoiceNum(String.valueOf(invoiceInfoService.count(
                        Wrappers.lambdaQuery(InvoiceInfo .class)
                                .eq(InvoiceInfo::getIsDel,0)
                                .eq(InvoiceInfo::getCaseId,item.getId())
                                .eq(InvoiceInfo::getCaseOrCustomer,2)
                )));
            }
        }
        return page;
    }

    public void saveOrUp(AgreementCustomerVO entity) {
        SysUser loginUser = userService.getUser();
        Date now = new Date();

        entity.setUpdateTime(now);
        entity.setUpdateUser(loginUser.getId());
        // 服务期
        if (now.after(entity.getStarTime()) && now.before(entity.getEndTime())) {
            entity.setInService(1);
        }else {
            entity.setInService(0);
        }

        if (Objects.isNull(entity.getId())) {

            Long reCount = agreementCustomerMapper.selectCount(Wrappers.<AgreementCustomer>lambdaQuery()
                    .eq(AgreementCustomer::getIsDel, 0)
                    .eq(AgreementCustomer::getCreditCode, entity.getCreditCode())
                    .le(AgreementCustomer::getStarTime, entity.getEndTime())
                    .ge(AgreementCustomer::getEndTime, entity.getStarTime())
            );
            if (reCount > 0) {
                throw new CustomException("社会统一信用代码重复");
            }
            Long nameCount = agreementCustomerMapper.selectCount(Wrappers.<AgreementCustomer>lambdaQuery()
                    .eq(AgreementCustomer::getIsDel, 0)
                    .eq(AgreementCustomer::getCustomerName, entity.getCustomerName())
                    .le(AgreementCustomer::getStarTime, entity.getEndTime())
                    .ge(AgreementCustomer::getEndTime, entity.getStarTime())
            );
            if (nameCount > 0) {
                throw new CustomException("客户名称重复");
            }
            Long count = agreementCustomerMapper.selectCount(new LambdaQueryWrapper<>());
            entity.setCustomerCode(
                    decimalFormat.format(count + 1));
            entity.setIsDel(0);
            entity.setCreateTime(now);
            entity.setCreateUser(loginUser.getId());
            entity.setParentId(0);
            entity.setInvoice("1");
            agreementCustomerMapper.insert(entity);
            if (CollUtil.isEmpty(entity.getLawyers())) {
                throw new CustomException("请添加律师");
            }
            entity.getLawyers().forEach(l -> {
                CustomerLawyers cl = new CustomerLawyers();
                cl.setCustomerId(entity.getId());
                cl.setLawyerId(l);
                cl.insert();
            });

            if (CollUtil.isEmpty(entity.getFiles())) {
                throw new CustomException("请上传文件");
            }
            this.sysFileService.<SysFile>lambdaUpdate()
                    .set(SysFile::getBizId, entity.getId())
                    .eq(SysFile::getIsDel, CommonConstants.ISDEL_NO)
                    .in(SysFile::getId, entity.getFiles().stream().map(SysFile::getId).collect(Collectors.toList()))
                    .update();
        }else {
            if (CollUtil.isNotEmpty(entity.getDelIds())) {
                for (String delId : entity.getDelIds()) {
                    AgreementCustomer agreementCustomer = agreementCustomerMapper.selectById(delId);
                    agreementCustomer.setIsDel(1);
                    agreementCustomerMapper.updateById(agreementCustomer);
                }
            }
        }
        if (CollUtil.isNotEmpty(entity.getAcList())) {
            entity.getAcList().forEach(ac -> {
                ac.setInService(entity.getInService());
                ac.setStarTime(entity.getStarTime());
                ac.setEndTime(entity.getEndTime());
                ac.setIsDel(0);
                ac.setCreateTime(now);
                ac.setCreateUser(loginUser.getId());
                ac.setParentId(entity.getId());
                Long reCount = agreementCustomerMapper.selectCount(Wrappers.<AgreementCustomer>lambdaQuery()
                        .eq(AgreementCustomer::getIsDel, 0)
                        .eq(AgreementCustomer::getCreditCode, ac.getCreditCode())
                        .le(AgreementCustomer::getStarTime, entity.getEndTime())
                        .ge(AgreementCustomer::getEndTime, entity.getStarTime())
                );
                if (reCount > 0) {
                    throw new CustomException("社会统一信用代码重复");
                }
                Long nameCount = agreementCustomerMapper.selectCount(Wrappers.<AgreementCustomer>lambdaQuery()
                        .eq(AgreementCustomer::getIsDel, 0)
                        .eq(AgreementCustomer::getCustomerName, ac.getCustomerName())
                        .le(AgreementCustomer::getStarTime, entity.getEndTime())
                        .ge(AgreementCustomer::getEndTime, entity.getStarTime())
                );
                if (nameCount > 0) {
                    throw new CustomException("客户名称重复");
                }
                agreementCustomerMapper.insert(ac);
                entity.getLawyers().forEach(l -> {
                    CustomerLawyers cl = new CustomerLawyers();
                    cl.setCustomerId(ac.getId());
                    cl.setLawyerId(l);
                    cl.insert();
                });
            });
        }
    }

    public void delById(AgreementCustomer entity) {
        SysUser loginUser = userService.getUser();
        Date now = new Date();
        entity.setUpdateTime(now);
        entity.setUpdateUser(loginUser.getId());
        entity.setIsDel(1);
        agreementCustomerMapper.updateById(entity);
    }

    public List<AgreementCustomer> list() {
        SysUser user = userService.getUser();
        return agreementCustomerMapper.getList(user.getId());
    }


    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    public ResultInfo saveOrUpdateInvoiceInfo(InvoiceInfoVo bean) {
        SysUser user = userService.getUser();
        AgreementCustomer ac = this.getById(bean.getCaseId());
        ac.setInvoice(InvoiceEnums.PENDING_APPROVAL.getValue());
        this.updateById(ac);
        if (StringUtils.isEmpty(bean.getId())){
            bean.setIsDel(CommonConstants.ISDEL_NO+"");
            bean.setCreateTime(DateUtil.format(new Date()));
            bean.setCaseOrCustomer("2");
            bean.setCreateUser(user.getId().toString());
            this.invoiceInfoService.saveOrUpdate(bean);
            approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),bean.getInvoiceMoney(),user,
                    bean.getId(),4);
            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessApproveRecord::getCaseId, ac.getId()).ne(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
            if (processApproveRecords.isEmpty()){//数量为1时  就是最后一个节点通过  案件 发票状态修改为已开票
                ac.setInvoice(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                this.updateById(ac);
            }
        }else {
            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessApproveRecord::getStatus,2).eq(ProcessApproveRecord::getCaseId,bean.getCaseId()).eq(ProcessApproveRecord::getInvoiceInfoId,bean.getId());
            approveRecordService.remove(wrapper);
            approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),bean.getInvoiceMoney(),user,
                    bean.getId(),4);
//            bean.setCreateTime(DateUtil.formatDate(new Date()));
//            bean.setCreateUser(user.getId().toString());
            this.invoiceInfoService.saveOrUpdate(bean);
        }


        return ResultInfo.success();
    }

    public AgreementCustomerVO detail(Integer id) {
        AgreementCustomer ac = this.getById(id);
        AgreementCustomerVO vo = new AgreementCustomerVO();
        BeanUtils.copyProperties(ac,vo);
        vo.setFiles(sysFileService.list(Wrappers.lambdaQuery(SysFile.class)
                .eq(SysFile::getBizType, 5).eq(SysFile::getBizId, id)));
        vo.setAcList(this.agreementCustomerMapper
                .selectList(Wrappers.lambdaQuery(AgreementCustomer.class)
                        .eq(AgreementCustomer::getParentId, id).eq(AgreementCustomer::getIsDel,0)));
        List<CustomerLawyers> list = this.customerLawyersService.list(Wrappers.lambdaQuery(CustomerLawyers.class)
                .eq(CustomerLawyers::getCustomerId, id));
        List<Integer> collect = list.stream().map(CustomerLawyers::getLawyerId).collect(Collectors.toList());
        vo.setLawyers(collect);

        vo.setLawyerNames(userMapper
                .selectList(Wrappers.lambdaQuery(SysUser.class).in(SysUser::getId, collect))
                .stream().map(SysUser::getRealName)
                .collect(Collectors.joining(","))
        );
        return vo;
    }

    public ResultInfo invoiceInfoDetail(Integer id) {
        List<InvoiceInfo> list =
                this.invoiceInfoService.list(Wrappers.lambdaQuery(InvoiceInfo.class).eq(InvoiceInfo::getCaseId
                , id).eq(InvoiceInfo::getCaseOrCustomer, "2").orderByDesc(InvoiceInfo::getCreateTime));
        if (CollUtil.isEmpty(list)){
            throw new CustomException("未开票");
        }
        return this.getEntityById(list);
    }

    /**
     * 根据id获取类 todo
     *
     * @param l
     * @return
     */
    public ResultInfo getEntityById(List<InvoiceInfo> l) {
        List<InvoiceInfoVo> r = new ArrayList<>();
        for (InvoiceInfo invoiceInfo : l) {
            Integer id = invoiceInfo.getId();
            LambdaQueryWrapper<SysDict> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(SysDict::getDictType,"fileType").eq(SysDict::getMemo,"INVOICE");
            SysDict one = sysDictService.getOne(wrapper);
            InvoiceInfoVo vo = new InvoiceInfoVo();
            BeanUtil.copyProperties(this.invoiceInfoService.getById(id), vo);
            LambdaQueryWrapper<SysFile> wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(SysFile::getBizId,id).in(SysFile::getBizType,one.getDictValue()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO);
            List<SysFile> list = sysFileService.list(wrapper1);
            vo.setFileList(list);
            if (vo.getCaseOrCustomer().equals("1")){
                CaseInfo caseInfo = caseInfoMapper.selectById(vo.getCaseId());
                vo.setBack(caseInfo.getInvoiceBack());
            }else{
                AgreementCustomer customer = this.getById(vo.getCaseId());
                vo.setBack(customer.getReturnRemarks());
            }
            r.add(vo);
        }

        return ResultInfo.success(r);
    }

    public List<AgreementCustomer> getListByRole() {
        SysUser user = userService.getUser();
        List<String> codeList = user.getRoleList().stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        // 处理空列表或null（若空列表算作“不满足仅包含ls”，则返回false）
        if (CollUtil.isEmpty(codeList))  {
            return agreementCustomerMapper.selectList(Wrappers.lambdaQuery(AgreementCustomer.class).eq(AgreementCustomer::getIsDel, CommonConstants.ISDEL_NO));
        }

        // 检查所有元素是否均为"ls"
        if (codeList.stream().allMatch(CommonConstants.NORMAL_SOLICITOR::equals)){
            return agreementCustomerMapper.getList(user.getId());
        }
        return agreementCustomerMapper.selectList(Wrappers.lambdaQuery(AgreementCustomer.class).eq(AgreementCustomer::getIsDel, CommonConstants.ISDEL_NO));
    }
}
