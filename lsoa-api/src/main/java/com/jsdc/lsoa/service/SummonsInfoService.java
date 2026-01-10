package com.jsdc.lsoa.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jsdc.core.base.BaseService;
import com.jsdc.core.common.utils.DateUtil;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.enums.InvoiceEnums;
import com.jsdc.lsoa.enums.PayEnums;
import com.jsdc.lsoa.enums.SummonsEnums;
import com.jsdc.lsoa.mapper.CaseInfoMapper;
import com.jsdc.lsoa.mapper.SummonsInfoMapper;
import com.jsdc.lsoa.mapper.SysUserRoleMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SummonsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SummonsInfoService extends BaseService<SummonsInfo>  {

    @Autowired
    private SummonsInfoMapper summonsInfoMapper;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private ProcessApproveRecordService approveRecordService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private AgreementCustomerService agreementCustomerService;

    @Autowired
    private CaseInfoService caseInfoService;
    /**
     * 分页查询 todo
     *
     * @return
     */
    public Page<CaseInfoVo> getPageList(CaseInfoVo vo) {
        SysUser user = sysUserService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        boolean c = list.stream().anyMatch(a -> a.getRoleCode().equals("010"));
        boolean b = list.stream().anyMatch(a -> a.getRoleCode().equals("011"));
        boolean a = list.stream().anyMatch(u -> u.getRoleCode().equals("admin1"));
        if (!b&&!a&&!c){
            vo.setCreateUser(user.getId()+"");
            vo.setApproveUser(user.getId()+"");
            vo.setApproveRole(list);
        }
        Page<CaseInfoVo> page = summonsInfoMapper.selectSummonsPage(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        page.getRecords().forEach(record -> {
            //查询审批人的名单
            LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ProcessApproveRecord::getCaseId, record.getId()).eq(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getSummonsInfoId,record.getSummonsId()).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
            if (processApproveRecords.size()>0){
                record.setApproveIds(processApproveRecords.get(0));
            }
            record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
        });
        return page;

    }

    /**
     * 查询 todo
     *
     * @return
     */
    public List<SummonsInfoVo> getList(CaseInfoVo bean) {
        List<SummonsInfoVo> summonsInfos = summonsInfoMapper.getEntityList(bean);
        CaseInfo caseInfo = caseInfoMapper.selectById(bean.getId());
        summonsInfos.forEach(record -> {
//            LambdaQueryWrapper<SysDict> wrapper=new LambdaQueryWrapper<>();
//            wrapper.eq(SysDict::getDictType,"printType").eq(SysDict::getDictValue,record.getPrintType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);
//            SysDict one = sysDictService.getOne(wrapper);
//            record.setPrintTypeName(one.getDictLabel());
            //查询审批人的名单
            LambdaUpdateWrapper<ProcessApproveRecord> wrapper1 = new LambdaUpdateWrapper<>();
            wrapper1.eq(ProcessApproveRecord::getCaseId, bean.getId()).eq(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getSummonsInfoId,record.getId()).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper1);
            if (CollUtil.isNotEmpty(processApproveRecords)){
                record.setApproveIds(processApproveRecords.get(0));
            }
            LambdaQueryWrapper<SysFile> fileWrapper=new LambdaQueryWrapper<>();
            fileWrapper.eq(SysFile::getIsDel,CommonConstants.ISDEL_NO).eq(SysFile::getBizId,caseInfo.getId()).eq(SysFile::getBizType,2);
            List<SysFile> caseFiles = sysFileService.list(fileWrapper);
            record.setCaseFiles(caseFiles);
            record.setUserName(sysUserService.getById(Integer.parseInt(caseInfo.getCreateUser())).getRealName());
            record.setCreateUser(caseInfo.getCreateUser());
        });
        return summonsInfos;
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    public ResultInfo saveOrUpdateSummonsInfo(SummonsInfoVo bean) {
        SysUser user = sysUserService.getUser();
//        bean.setState(InvoiceEnums.PENDING_APPROVAL.getValue());
        CaseInfo caseInfo = caseInfoMapper.selectById(bean.getCaseId());
        caseInfo.setLetter(InvoiceEnums.PENDING_APPROVAL.getValue());
        if (!StringUtils.isEmpty(caseInfo.getCustomerId())){
            AgreementCustomer agreementCustomer = agreementCustomerService.getById(caseInfo.getCustomerId());
            if (agreementCustomer.getEndTime().compareTo(new Date())<0){
                return ResultInfo.error("协议客户已逾期。");
            }
        }
        caseInfo.setLetterBack("");
        caseInfoMapper.updateById(caseInfo);
        if (StringUtils.isEmpty(bean.getId())){
            bean.setIsDel(CommonConstants.ISDEL_NO+"");
            bean.setCreateTime(DateUtil.formatDate(new Date()));
            bean.setCreateUser(user.getId()+"");
            bean.setSummonsTime(DateUtil.formatDate(new Date()));
            saveOrUpdate(bean);
            approveRecordService.generatorLetterRecord(caseInfo.getId(),user,bean.getId());
//            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).eq(ProcessApproveRecord::getProcessType,1)
//                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getSummonsInfoId,bean.getId()).orderByAsc(ProcessApproveRecord::getSort);
//            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
//            if (processApproveRecords.size()>0&&StringUtils.isEmpty(caseInfo.getCustomerId())){//数量为1时  就是最后一个节点通过  案件 发票状态修改为已开票
////                invoiceInfo.setState(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
//                caseInfo.setInvoice(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
//                caseInfoMapper.updateById(caseInfo);
//            }
           if (CollUtil.isNotEmpty(bean.getFiles())){
               LambdaUpdateWrapper<SysFile> wrapper1 = new LambdaUpdateWrapper<>();
               wrapper1.set(SysFile::getBizId,bean.getId()).in(SysFile::getId,  bean.getFiles()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO);
               sysFileService.update(wrapper1);
           }
        }else {
            if (CollUtil.isNotEmpty(bean.getFiles())) {
                LambdaUpdateWrapper<SysFile> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(SysFile::getBizId, bean.getId()).in(SysFile::getId,  bean.getFiles()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO);
                sysFileService.update(wrapper);
                wrapper.clear();
                wrapper.eq(SysFile::getBizId, bean.getId()).notIn(SysFile::getId, bean.getFiles()).set(SysFile::getIsDel, CommonConstants.ISDEL_YES);
                sysFileService.update(wrapper);
            }

            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessApproveRecord::getStatus,2).eq(ProcessApproveRecord::getCaseId,bean.getCaseId()).eq(ProcessApproveRecord::getSummonsInfoId,bean.getId());
            approveRecordService.remove(wrapper);

            bean.setCreateTime(DateUtil.formatDate(new Date()));
            bean.setSummonsTime(DateUtil.formatDate(new Date()));
            approveRecordService.generatorLetterRecord(caseInfo.getId(),user,bean.getId());
            saveOrUpdate(bean);
        }
        return ResultInfo.success();
    }

    /**
     * 根据id获取类 todo
     *
     * @param id
     * @return
     */
    public ResultInfo getEntityById(Integer id) {
        LambdaQueryWrapper<SysDict> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType,"fileType").eq(SysDict::getMemo,"LETTER").eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);
        SysDict one = sysDictService.getOne(wrapper);
        SummonsInfoVo vo = new SummonsInfoVo();
        BeanUtil.copyProperties(getById(id), vo);
        LambdaQueryWrapper<SysFile> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(SysFile::getBizId,id).eq(SysFile::getBizType,one.getDictValue()).eq(SysFile::getIsDel,0);
        List<SysFile> list = sysFileService.list(wrapper1);
        vo.setFileList(list);
        wrapper.clear();
//        wrapper.eq(SysDict::getDictType,"printType").eq(SysDict::getDictValue,vo.getPrintType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);;
//        SysDict dict = sysDictService.getOne(wrapper);
        CaseInfoVo caseInfoVo = caseInfoService.getEntity(Integer.valueOf(vo.getCaseId()));
//        vo.setPrintTypeName(dict.getDictLabel());
        vo.setInfo(caseInfoVo);
        vo.setBack(caseInfoVo.getLetterBack());
        return ResultInfo.success(vo);
    }

    public ResultInfo approveInvoiceInfo(SummonsInfoVo vo) {
        SummonsInfo summonsInfo = getById(vo.getId());
        CaseInfo caseInfo = caseInfoMapper.selectById(summonsInfo.getCaseId());
        if (vo.getApproveStatus().equals("1")){//通过
            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).eq(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getSummonsInfoId,vo.getId()).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
            ProcessApproveRecord processApproveRecord = processApproveRecords.get(0);
            processApproveRecord.setStatus(1);
            approveRecordService.updateById(processApproveRecord);
            if (processApproveRecords.size() == 1){//数量为1时  就是最后一个节点通过  案件 开函状态修改为已开
//                summonsInfo.setState(SummonsEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                caseInfo.setLetter(SummonsEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
//                updateById(summonsInfo);
                caseInfoMapper.updateById(caseInfo);
            }
        }else {//退回
            ProcessApproveRecord processApproveRecords =  new ProcessApproveRecord();
            processApproveRecords.setStatus(2);
            LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).eq(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getSummonsInfoId,vo.getId());
            approveRecordService.update(processApproveRecords,wrapper);//把所有节点改成未审批
//            summonsInfo.setState(SummonsEnums.RETURNED.getValue());
            caseInfo.setLetter(SummonsEnums.RETURNED.getValue());
//            updateById(summonsInfo);//案件 发票状态修改为退回
            caseInfo.setLetterBack(vo.getBack());
            caseInfoMapper.updateById(caseInfo);
        }
        return ResultInfo.success();
    }


    /**
     * 分页查询 todo
     *
     * @return
     */
    public Page<CaseInfoVo> selectAPPSummonsPage(CaseInfoVo vo) {
        SysUser user = sysUserService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        boolean c = list.stream().anyMatch(a -> a.getRoleCode().equals("010"));
        boolean b = list.stream().anyMatch(a -> a.getRoleCode().equals("011"));
        boolean a = list.stream().anyMatch(u -> u.getRoleCode().equals("admin1"));
        if (!b&&!a&&!c){
            vo.setCreateUser(user.getId()+"");
            vo.setApproveUser(user.getId()+"");
            vo.setApproveRole(list);
        }
        Page<CaseInfoVo> page = summonsInfoMapper.selectAPPSummonsPage(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        page.getRecords().forEach(record -> {
            //查询审批人的名单
            LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ProcessApproveRecord::getCaseId, record.getId()).eq(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getSummonsInfoId,record.getSummonsId()).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
            if (CollUtil.isNotEmpty(processApproveRecords)){
                record.setApproveIds(processApproveRecords.get(0));
            }
            record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
        });
        return page;

    }



    /**
     * 导出
     */
    public void export(CaseInfoVo vo, HttpServletResponse response) {
        // 查询数据
        SysUser user = sysUserService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        boolean c = list.stream().anyMatch(a -> a.getRoleCode().equals("010"));
        boolean b = list.stream().anyMatch(a -> a.getRoleCode().equals("011"));
        boolean a = list.stream().anyMatch(u -> u.getRoleCode().equals("admin1"));
        if (!b&&!a&&!c){
            vo.setCreateUser(user.getId()+"");
            vo.setApproveUser(user.getId()+"");
            vo.setApproveRole(list);
        }
        List<CaseInfoVo> records = summonsInfoMapper.selectSummonsList( vo);
        List<SummonsInfo> summonsInfos=new ArrayList<>();
        records.forEach(record -> {
            record.setLetter(InvoiceEnums.getDescByValue(record.getLetter()));
            record.setLetterType(PayEnums.getDescByValue(record.getLetterType()));
            record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
            LambdaQueryWrapper<SummonsInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SummonsInfo::getCaseId,record.getId());
            List<SummonsInfo> summonsInfos1 = summonsInfoMapper.selectList(wrapper);
//            summonsInfos1.forEach(summonsInfo -> {
//                LambdaQueryWrapper<SysDict> wrapper1=new LambdaQueryWrapper<>();
//                wrapper1.eq(SysDict::getDictType,"printType").eq(SysDict::getDictValue,summonsInfo.getPrintType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);
//                SysDict one = sysDictService.getOne(wrapper1);
//                summonsInfo.setPrintType(one.getDictLabel());
//            });
            summonsInfos.addAll(summonsInfos1);
        });
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("caseNum", "案件编号");
//        writer.addHeaderAlias("caseName", "案件名称");
        writer.addHeaderAlias("userName", "承办律师");
        writer.addHeaderAlias("client", "委托人");
        writer.addHeaderAlias("tarClient", "对方当事人");
        writer.addHeaderAlias("subjectOfAction", "案由");
        writer.addHeaderAlias("court", "办案机构");
//        writer.addHeaderAlias("alarmStatusName", "委托方式");
        writer.addHeaderAlias("letterType", "开函类型");
        writer.addHeaderAlias("summonsTime", "开函时间");
        writer.addHeaderAlias("contractMoney", "合同金额");
        writer.addHeaderAlias("invoiceMoney", "开票金额");
        writer.addHeaderAlias("invoiceStr", "发票类型");
        writer.addHeaderAlias("contactPerson", "联系人");
        writer.addHeaderAlias("contactPhone", "联系人电话");
        writer.addHeaderAlias("mail", "电子邮箱");
        writer.addHeaderAlias("addressI", "收件地址");
        writer.addHeaderAlias("applyTime", "申请开票时间");
        writer.addHeaderAlias("invoiceTime", "开票时间");
        writer.addHeaderAlias("letter", "审核状态");

//        writer.addHeaderAlias("content", "发票内容");
//        writer.addHeaderAlias("num", "份数");
//        writer.addHeaderAlias("name", "单位名称/个人名称");
//        writer.addHeaderAlias("code", "统一代码/身份证号");
//        writer.addHeaderAlias("phone", "联系电话");
//        writer.addHeaderAlias("printType", "用印类型");
//        writer.addHeaderAlias("useing", "用途");
//        writer.addHeaderAlias("addressType", "收件方");
//        writer.addHeaderAlias("addressee", "收件单位名称");
//        writer.addHeaderAlias("address", "收件地址");
//        writer.addHeaderAlias("addresseePhone", "收件人电话");
//        writer.addHeaderAlias("specialDemand", "特殊格式要求");
//        writer.addHeaderAlias("level", "紧急程度");
        writer.setOnlyAlias(true);
        writer.write(records, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("SummonsInfo.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
//            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}


