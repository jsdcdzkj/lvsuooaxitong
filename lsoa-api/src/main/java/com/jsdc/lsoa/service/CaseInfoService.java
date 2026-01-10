package com.jsdc.lsoa.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.enums.CaseStatus;
import com.jsdc.lsoa.enums.InvoiceEnums;
import com.jsdc.lsoa.enums.LetterEnums;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.CaseInfoMapper;
import com.jsdc.lsoa.mapper.SysUserRoleMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.utils.RedisUtils;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SummonsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CaseInfoService extends BaseService<CaseInfo>  {

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private ClientInfoService clientInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ProcessApproveRecordService approveRecordService;
    @Autowired
    private SummonsInfoService summonsInfoService;
    @Autowired
    private InvoiceInfoService invoiceInfoService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private AgreementCustomerService customerService;

    /**
     * 分页查询
     */
    public Page<CaseInfoVo> getPageList(CaseInfoVo vo) {

        SysUser sysUser = sysUserService.getUser();
        // 列表展示所有的案件，支持根据权限控制（律师只能展示本人的案件，管理者可查看本人和下属的案件）；所有用户可以新增案件；
        List<SysRole> roleList = sysUser.getRoleList();
        vo.setCurrentUserId(sysUser.getId().toString());
        List<String> collect = roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        if (collect.contains(CommonConstants.ADMIN)){

        }else if (collect.contains(CommonConstants.DIRECTOR)){
            vo.setDeptId(sysUser.getDeptId().toString());
        }else {
            vo.setCreateUser(sysUser.getId().toString());
        }
        Page<CaseInfoVo> page = caseInfoMapper.selectPageWhere(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        for (CaseInfoVo record : page.getRecords()) {
            record.setStatusStr(CaseStatus.getDescByValue(record.getStatus()));
            record.setInvoiceStr(InvoiceEnums.getDescByValue(record.getInvoice()));
            record.setLetterStr(LetterEnums.getDescByValue(record.getLetter()));
            record.setSummonsNum(String.valueOf(summonsInfoService.count(
                    Wrappers.lambdaQuery(SummonsInfo.class)
                            .eq(SummonsInfo::getIsDel,0)
                            .eq(SummonsInfo::getCaseId,record.getId())
            )));
            record.setInvoiceNum(String.valueOf(invoiceInfoService.count(
                    Wrappers.lambdaQuery(InvoiceInfo.class)
                            .eq(InvoiceInfo::getIsDel,0)
                            .eq(InvoiceInfo::getCaseId,record.getId())
                            .eq(InvoiceInfo::getCaseOrCustomer,1)
            )));
        }
        return page;
    }


    public ResultInfo saveCaseInfo(CaseInfoVo vo) {
        SysUser user = sysUserService.getUser();
        List<SysRole> roleList = user.getRoleList();
        List<String> collect = roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        if (!collect.contains(CommonConstants.NORMAL_SOLICITOR)){
            return ResultInfo.error("您的角色暂不支持创建案件！");
        }
        //案件
        String startTime = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = LocalDate.now().atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        long count = this.count(Wrappers.<CaseInfo>lambdaQuery().eq(CaseInfo::getIsDel, CommonConstants.ISDEL_NO)
                .between(CaseInfo::getCreateTime, startTime, endTime));
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        count = count + 1;
        String order = "C"+date+String.format("%03d", count);
        vo.setCaseNum(StrUtil.isBlank(vo.getCaseNum())?order:vo.getCaseNum());
        vo.setIsDel(CommonConstants.ISDEL_NO);
        vo.setCreateUser(user.getId().toString());
        vo.setDeptId(user.getDeptId().toString());
        vo.setCreateTime(DateUtil.now());
        if (StrUtil.isNotBlank(vo.getDelegate())){
            if (vo.getDelegate().equals("1")){
                vo.setInvoice("1");
            }else {
                vo.setInvoice("2");
            }
            vo.setLetter("2");
        }
        vo.setArchived("1");
        if (null == vo.getId()){
            this.save(vo);
        }else {
            this.sysFileService.<SysFile>lambdaUpdate()
                    .set(SysFile::getIsDel, CommonConstants.ISDEL_YES)
                    .eq(SysFile::getBizId, vo.getId())
                    .update();
            this.updateById(vo);
        }

        if (vo.getTemporarily().equals("2")){
            if (vo.getDelegate().equals("2")){
                InvoiceInfoVo invoiceInfoVo = vo.getInvoiceInfoVo();
                invoiceInfoVo.setCaseId(vo.getId().toString());
                invoiceInfoService.saveOrUpdateInvoiceInfo(vo.getInvoiceInfoVo());
            }
            SummonsInfoVo summonsInfoVo = vo.getSummonsInfoVo();
            summonsInfoVo.setCaseId(vo.getId().toString());
            summonsInfoService.saveOrUpdateSummonsInfo(vo.getSummonsInfoVo());
        }else {
            vo.setInvoiceJson(JSONObject.toJSONString(vo.getInvoiceInfoVo()));
            vo.setSummonsJson(JSONObject.toJSONString(vo.getSummonsInfoVo()));
            this.updateById(vo);
        }
        // 当事人
        this.saveClient(vo.getClientInfoVoList(), vo);
        this.updateById(vo);
        if (vo.getTemporarily().equals("2")){
            // 更新文件
            if (CollUtil.isEmpty(vo.getFileIdList())){
                if (Objects.equals("2",vo.getDelegate())){
                    throw new CustomException("请上传文件！");
                }else {
                    return ResultInfo.success(vo);
                }
            }
        }else {
            // 更新文件
            if (CollUtil.isEmpty(vo.getFileIdList())){
                return ResultInfo.success(vo);
            }
        }
        this.updateFile(vo);
        return ResultInfo.success(vo);
    }
    public void updateFile(CaseInfoVo vo){
        this.sysFileService.<SysFile>lambdaUpdate()
                .set(SysFile::getBizId, vo.getId())
                .set(SysFile::getIsDel, CommonConstants.ISDEL_NO)
                .in(SysFile::getId, vo.getFileIdList())
                .update();

        List<SysDict> fileType = sysDictService.getListByType("fileType")
                .stream()
                .filter(x->x.getMemo().equals("CASE")).filter(x->x.getDictLabel().equals("合同")).collect(Collectors.toList());
        if (CollUtil.isEmpty(fileType)){
            throw new CustomException("未查询到合同文件类型！");
        }
        SysDict sysDict = fileType.get(0);
        List<SysFile> list = this.sysFileService.list(Wrappers.lambdaQuery(SysFile.class)
                .eq(SysFile::getBizId, vo.getId())
                .eq(SysFile::getIsDel, CommonConstants.ISDEL_NO)
                .eq(SysFile::getBizType, sysDict.getDictValue()));
        if (CollUtil.isEmpty(list)){
            throw new CustomException("请上传合同文件！");
        }
    }

    /**
     * 插入当事人并返回当事人名称
     */
    public void saveClient(List<ClientInfo> clientInfoVoList,CaseInfoVo vo){
        //删除当事人
        if (CollUtil.isNotEmpty(vo.getClientDelIds())){
            clientInfoService.remove(Wrappers.lambdaQuery(ClientInfo.class).in(ClientInfo::getId,vo.getClientDelIds()));
        }
        if (vo.getTemporarily().equals("1")){
            if (CollUtil.isEmpty(clientInfoVoList)) {
                return;
            }
        }else {
            if (CollUtil.isEmpty(clientInfoVoList)) {
                throw new CustomException("当事人信息不能为空");
            }
        }
        Set<String> customerIds = new HashSet<>();
        for (ClientInfo client : clientInfoVoList) {
            if (null == client.getCustomerId()){
                continue;
            }
            String customerId = client.getCustomerId();
            if (customerIds.contains(customerId))  {
                System.out.println(" 提示：存在相同的协议客户，customerId: " + customerId);
                throw new CustomException("存在相同的协议客户");
            } else {
                customerIds.add(customerId);
            }
        }
        // 如果不是已完结，就检查当事人
        if (!Objects.equals(vo.getStatus(),"2")){
            checkAgentConflict(clientInfoVoList, vo);
        }
        for (ClientInfo clientInfo : clientInfoVoList) {
            clientInfo.setCaseId(vo.getId().toString());
            clientInfo.setIsDel(CommonConstants.ISDEL_NO);
            if (clientInfo.getId() == null) {
                clientInfoService.save(clientInfo);
            }else {
                clientInfoService.updateById(clientInfo);
            }
        }
        // 使用 Stream 将 name 字段收集成以逗号分隔的字符串
        vo.setClient(clientInfoVoList.stream().filter(x->x.getPlainOrDefend().equals("1"))
                .map(ClientInfo::getName) // 提取 name 字段
                .collect(Collectors.joining(",")));// 用逗号和空格连接);
        vo.setTarClient(clientInfoVoList.stream().filter(x->x.getPlainOrDefend().equals("2"))
                .map(ClientInfo::getName) // 提取 name 字段
                .collect(Collectors.joining(",")));// 用逗号和空格连接);

    }

    /**
     * 委托人与对方委托人
     * 不等于已完结的案件都要筛选
     * 根据《中华人民共和国律师法》第三十九条的规定 同一律所的律师不能同时代理原被告。
     * 律所同一案件不能被两个律师同时代理。
     */
    public void checkAgentConflict(List<ClientInfo> clientInfoVoList, CaseInfoVo vo) {
        // 新增参数校验
        if (CollUtil.isEmpty(clientInfoVoList)) {
            throw new CustomException("当事人信息不能为空");
        }
        // 所有未完结的案件
        List<CaseInfo> caseInfos = this.list(Wrappers.<CaseInfo>lambdaQuery()
                .eq(CaseInfo::getIsDel,CommonConstants.ISDEL_NO)
                // 排除自己
                .ne(null != vo.getId(),CaseInfo::getId,vo.getId())
                .ne(CaseInfo::getStatus, CaseStatus.FINISHED.getValue())
        );
        // 如果没有未完结的直接过
        if(CollUtil.isNotEmpty(caseInfos)){
            List<Integer> caseIds = caseInfos.stream().map(CaseInfo::getId).collect(Collectors.toList());
            // 得到所有未完结的案件的委托人（所有当事人）用本次传入的当事人 与其对比
            List<ClientInfo> list = this.clientInfoService.<ClientInfo>lambdaQuery()
                    .eq(ClientInfo::getIsDel, CommonConstants.ISDEL_NO)
                    .in(ClientInfo::getCaseId, caseIds)
                    .list();
            Map<String, List<ClientInfo>> clientMap = list.stream().collect(Collectors.groupingBy(ClientInfo::getCaseId));
            // 循环案件比对当事人
            Set<String> principal = getClient(clientInfoVoList,"1");// 委托人
            if (CollUtil.isEmpty(principal)) {
                throw new CustomException("委托人不能为空");
            }
            // 刑事案件，对方当事人可以不传
            Set<String> target = getClient(clientInfoVoList,"2");// 对方委托人
            if (CollUtil.isEmpty(target)) {
                if (!Objects.equals("1",vo.getCaseType())){
                    throw new CustomException("对方委托人不能为空");
                }
            }
            if (!Collections.disjoint(principal, target)) {
                throw new CustomException("当前案件委托人与对方委托人存在相同人员！");
            }
            for (CaseInfo caseInfo : caseInfos) {
                //某案件的所有当事人
                List<ClientInfo> clientInfos = clientMap.get(caseInfo.getId().toString());
                if (clientInfos == null) {
                    continue;
                }
                Set<String> principalOther = getClient(clientInfos,"1");// 委托人
                Set<String> targetOther = getClient(clientInfos,"2");// 对方委托人
                boolean a = false;
                boolean b = false;
                boolean c = false;
                boolean d = false;
                // 委托人 与 其他案件对方委托人 有交集
                if (!CollUtil.intersection(principal, targetOther).isEmpty()){
                    a = true;
                }
                // 对方委托人 与 其他案件委托人 有交集
                if (!CollUtil.intersection(target, principalOther).isEmpty()){
                    b = true;
                }
                // 委托人 与 其他案件委托人 有交集
                if (!CollUtil.intersection(principal, principalOther).isEmpty()){
                    c = true;
                }
                // 对方委托人 与 其他案件对方委托人 有交集
                if (!CollUtil.intersection(target, targetOther).isEmpty()){
                    d = true;
                }
                if (a && b){
                    throw new CustomException("同一律所不能同时代理原被告！");
                }
                if (a){
                    throw new CustomException("此案件委托人与其他案件对方委托人重复！");
                }
                if (b){
                    throw new CustomException("此案件对方委托人与其他案件委托人重复！");
                }
                if (c && d && Objects.equals(caseInfo.getSubjectOfAction(),vo.getSubjectOfAction())){
                    throw new CustomException("当前委托人与对方委托人已有案件！");
                }

            }
        }
    }

    private Set<String> getClient(List<ClientInfo> clientInfoVoList,String type) {
        return clientInfoVoList.stream()
                .filter(Objects::nonNull)
                .filter(x-> Objects.equals(type,x.getPlainOrDefend()))
                .map(ClientInfo::getName)
                .collect(Collectors.toSet());
    }


    public void updateCaseInfo(CaseInfoVo vo) {
        vo.setUpdateUser(sysUserService.getUser().getId().toString());
        vo.setUpdateTime(DateUtil.now());
        this.updateById(vo);
    }

    public void updateClientInfo(CaseInfoVo vo) {

        this.clientInfoService.<ClientInfo>lambdaUpdate()
                .set(ClientInfo::getIsDel, CommonConstants.ISDEL_YES)
                .eq(ClientInfo::getCaseId, vo.getId())
                .update();
        this.saveClient(vo.getClientInfoVoList(), vo);
        // 更新当事人字段
        CaseInfo caseInfo = this.getById(vo.getId());
        caseInfo.setClient(vo.getClient());
        caseInfo.setTarClient(vo.getTarClient());
        caseInfo.setUpdateUser(sysUserService.getUser().getId().toString());
        caseInfo.setUpdateTime(DateUtil.now());
        this.updateById(caseInfo);
    }

    public CaseInfoVo getEntity(Integer id) {
        CaseInfo caseInfo = this.getById(id);
        CaseInfoVo caseInfoVo = new CaseInfoVo();
        BeanUtil.copyProperties(caseInfo, caseInfoVo);
        List<ClientInfo> list = clientInfoService.list(Wrappers.<ClientInfo>lambdaQuery().eq(ClientInfo::getCaseId, caseInfo.getId()).eq(ClientInfo::getIsDel, CommonConstants.ISDEL_NO));
        caseInfoVo.setClientInfoVoList(list);
        List<SysFile> sysFiles = sysFileService.list(Wrappers.<SysFile>lambdaQuery().eq(SysFile::getBizId, caseInfo.getId()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO));
        // 按照 bizType 分组
        Map<Integer, List<SysFile>> groupedByBizType = sysFiles.stream()
                .collect(Collectors.groupingBy(SysFile::getBizType));
        caseInfoVo.setFileList(groupedByBizType);
        Map<String, List<ClientInfo>> collect = list.stream().collect(Collectors.groupingBy(ClientInfo::getPlainOrDefend));
        caseInfoVo.setPlainList(collect.get("1"));
        caseInfoVo.setDefendList(collect.get("2"));
        caseInfoVo.setUserName(sysUserService.getById(Integer.parseInt(caseInfo.getCreateUser())).getRealName());
        caseInfoVo.setStatusStr(CaseStatus.getDescByValue(caseInfoVo.getStatus()));
        Map<String, List<SysDict>> dictLists = (Map<String, List<SysDict>>) RedisUtils.getBeanValue(CommonConstants.allDictListPrefix);
        List<SysDict> litigationStage = dictLists.get("litigationStage");
        SysDict stageStr = litigationStage.stream().filter(x -> Objects.equals(x.getDictValue(), caseInfo.getStage())).findFirst().orElse(null);
        caseInfoVo.setStageStr(null == stageStr?"":stageStr.getDictLabel());
        List<SysDict> businessType = dictLists.get("businessType");
        SysDict businessTypeStr = businessType.stream().filter(x -> Objects.equals(x.getDictValue(), caseInfo.getBusinessType())).findFirst().orElse(null);
        caseInfoVo.setBusinessTypeStr(null == businessTypeStr?"":businessTypeStr.getDictLabel());
        List<SysDict> caseType = dictLists.get("caseType");
        SysDict caseTypeStr = caseType.stream().filter(x -> Objects.equals(x.getDictValue(), caseInfo.getCaseType())).findFirst().orElse(null);
        caseInfoVo.setCaseTypeStr(null == caseTypeStr?"":caseTypeStr.getDictLabel());
        return caseInfoVo;
    }

    public ResultInfo del(Integer id) {
        // 案件
        this.<CaseInfo>lambdaUpdate()
                .set(CaseInfo::getIsDel,CommonConstants.ISDEL_YES)
                .set(CaseInfo::getUpdateTime,DateUtil.now())
                .set(CaseInfo::getUpdateUser,sysUserService.getUser().getId().toString())
                .eq(CaseInfo::getId,id).update();
        // 当事人
        this.clientInfoService.<ClientInfo>lambdaUpdate()
                .set(ClientInfo::getIsDel, CommonConstants.ISDEL_YES)
                .eq(ClientInfo::getCaseId, id).update();
        return ResultInfo.success();
    }

    public void updateCaseFile(CaseInfoVo bean) {
        // 删除旧文件
        if (CollUtil.isNotEmpty(bean.getDelFileIdList())) {
            this.sysFileService.<SysFile>lambdaUpdate()
                    .set(SysFile::getIsDel, CommonConstants.ISDEL_YES)
                    .in(SysFile::getId, bean.getDelFileIdList())
                    .update();
        }
        // 新增文件
        if (CollUtil.isNotEmpty(bean.getNewFileIdList())) {
            this.sysFileService.<SysFile>lambdaUpdate()
                    .set(SysFile::getBizId, bean.getId())
                    .in(SysFile::getId, bean.getNewFileIdList())
                    .update();
        }
    }

    public ResultInfo editCase(CaseInfoVo vo) {
        this.updateCaseInfo(vo);
        this.updateClientInfo(vo);
        this.updateCaseFile(vo);
        return ResultInfo.success();
    }

    public ResultInfo toDo(CaseInfoVo vo){
        SysUser user = sysUserService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        vo.setApproveUser(user.getId()+"");
        vo.setApproveRole(list);
        Page<CaseInfoVo> page = caseInfoMapper.toDo(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        page.getRecords().forEach(record -> {



            if ("2".equals(record.getCaseOrCustomer())){
                record.setCust(customerService.detail(Integer.valueOf(record.getCaseId())));
                AgreementCustomer agreementCustomer = customerService.getById(record.getCaseId());
                //查询审批人的名单
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, agreementCustomer.getId()).eq(StringUtils.isEmpty(record.getSummonsId()),ProcessApproveRecord::getInvoiceInfoId,record.getInvoiceId())
                        .eq(StringUtils.isEmpty(record.getInvoiceId()),ProcessApproveRecord::getSummonsInfoId,record.getSummonsId())
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
                if (!processApproveRecords.isEmpty()){
                    record.setApproveIds(processApproveRecords.get(0));
                }
                record.setUserName(sysUserService.getById(agreementCustomer.getCreateUser()).getRealName());
            }else{
                record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
                //查询审批人的名单
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, record.getId()).eq(StringUtils.isEmpty(record.getSummonsId()),ProcessApproveRecord::getInvoiceInfoId,record.getInvoiceId())
                        .eq(StringUtils.isEmpty(record.getInvoiceId()),ProcessApproveRecord::getSummonsInfoId,record.getSummonsId())
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
                if (!processApproveRecords.isEmpty()){
                    record.setApproveIds(processApproveRecords.get(0));
                }
            }
        });
        return ResultInfo.success(page);
    }

    public ResultInfo finish(CaseInfoVo vo){
        SysUser user = sysUserService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        vo.setApproveUser(user.getId()+"");
        vo.setApproveRole(list);
        Page<CaseInfoVo> page = caseInfoMapper.finish(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        page.getRecords().forEach(record -> {


            if ("2".equals(record.getCaseOrCustomer())){
                record.setCust(customerService.detail(Integer.valueOf(record.getCaseId())));
                record.setUserName(sysUserService.getById(record.getCust().getCreateUser()).getRealName());
            }else{
                record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
            }
        });
        return ResultInfo.success(page);
    }

    public ResultInfo archive(Integer id) {
        SysUser user = sysUserService.getUser();
        List<String> collect = user.getRoleList().stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            if (collect.stream().anyMatch(x->Objects.equals(x,CommonConstants.XINGZHENG))){
                CaseInfo info = this.getById(id);
                info.setArchived("2");
                info.setUpdateTime(DateUtil.now());
                info.setUpdateUser(user.getId().toString());
                this.updateById(info);
                return ResultInfo.success();
            }
            return ResultInfo.error("您的角色暂不支持归档！");
        }
        return ResultInfo.error("您的角色暂不支持归档！");

    }
}


