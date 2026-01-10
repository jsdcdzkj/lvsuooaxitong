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
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.enums.InvoiceEnums;
import com.jsdc.lsoa.enums.PayEnums;
import com.jsdc.lsoa.mapper.CaseInfoMapper;
import com.jsdc.lsoa.mapper.InvoiceInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.lsoa.mapper.ProcessApproveRecordMapper;
import com.jsdc.lsoa.mapper.SysUserRoleMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.*;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceInfoService extends BaseService<InvoiceInfo>  {

    @Autowired
    private InvoiceInfoMapper invoiceInfoMapper;

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
    private ProcessDesignService processDesignService;

    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private AgreementCustomerService customerService;

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
        Page<CaseInfoVo> page = invoiceInfoMapper.selectInvoicePage(new Page<>(vo.getPageNo(), vo.getPageSize()), vo);
        page.getRecords().forEach(record -> {
            //查询详情
            if (record.getCaseOrCustomer().equals("2")){
                InvoiceInfo invoiceInfo = invoiceInfoMapper.selectById(record.getInvoiceId());
                record.setCust(customerService.detail(Integer.valueOf(invoiceInfo.getCaseId())));
                AgreementCustomer agreementCustomer = customerService.getById(record.getCaseId());
                //查询审批人的名单
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, agreementCustomer.getId()).ne(ProcessApproveRecord::getProcessType,1)
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
                if (!processApproveRecords.isEmpty()){
                    record.setApproveIds(processApproveRecords.get(0));
                }
                record.setUserName(sysUserService.getById(record.getCust().getCreateUser()).getRealName());
            }else{
                //查询审批人的名单
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, record.getId()).ne(ProcessApproveRecord::getProcessType,1)
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);
                if (!processApproveRecords.isEmpty()){
                    record.setApproveIds(processApproveRecords.get(0));
                }
                record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
            }
        });
        return page;

    }

    /**
     * 查询 todo
     *
     * @return
     */
    public List<InvoiceInfoVo>  getList(CaseInfoVo bean) {
        List<InvoiceInfoVo> infoVos = invoiceInfoMapper.getEntityList(bean);
        CaseInfo caseInfo = caseInfoMapper.selectById(bean.getId());
        AgreementCustomer agreementCustomer = customerService.getById(bean.getId());
        LambdaQueryWrapper<SysDict> fileType=new LambdaQueryWrapper<>();
        fileType.eq(SysDict::getDictType,"fileType").eq(SysDict::getMemo,"INVOICE");
        SysDict file = sysDictService.getOne(fileType);
        infoVos.forEach(record -> {
            LambdaQueryWrapper<SysFile> fileWrapper=new LambdaQueryWrapper<>();
            fileWrapper.eq(SysFile::getBizId,record.getId()).in(SysFile::getBizType,file.getDictValue()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO);
            List<SysFile> list = sysFileService.list(fileWrapper);
            record.setFileList(list);
            LambdaQueryWrapper<SysDict> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(SysDict::getDictType,"invoiceType").eq(SysDict::getDictValue,record.getInvoiceType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);
            SysDict one = sysDictService.getOne(wrapper);
            record.setInvoiceTypeName(one.getDictLabel());
            //查询审批人的名单
            LambdaUpdateWrapper<ProcessApproveRecord> wrapper1 = new LambdaUpdateWrapper<>();
            wrapper1.eq(ProcessApproveRecord::getCaseId, bean.getId()).ne(ProcessApproveRecord::getProcessType,1)
                    .eq(ProcessApproveRecord::getStatus,0).eq(ProcessApproveRecord::getInvoiceInfoId,record.getId()).orderByAsc(ProcessApproveRecord::getSort);
            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper1);
            if (!processApproveRecords.isEmpty()){
                record.setApproveIds(processApproveRecords.get(0));
            }
            if (record.getCaseOrCustomer().equals("1")){
                fileWrapper.clear();
                fileWrapper.eq(SysFile::getIsDel,CommonConstants.ISDEL_NO).eq(SysFile::getBizId,caseInfo.getId()).eq(SysFile::getBizType,2);
                List<SysFile> caseFiles = sysFileService.list(fileWrapper);
                record.setCaseFiles(caseFiles);
                record.setUserName(sysUserService.getById(Integer.parseInt(caseInfo.getCreateUser())).getRealName());
                record.setCreateUser(caseInfo.getCreateUser());
            }else {
                record.setUserName(agreementCustomer.getCustomerName());
                record.setCreateUser(agreementCustomer.getId()+"");
            }
        });
        return infoVos;
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    public ResultInfo saveOrUpdateInvoiceInfo(InvoiceInfoVo bean) {
        SysUser user = sysUserService.getUser();
//        bean.setState(InvoiceEnums.PENDING_APPROVAL.getValue());
        CaseInfo caseInfo = caseInfoMapper.selectById(bean.getCaseId());
        AgreementCustomer agreementCustomer = customerService.getById(bean.getCaseId());
        if (!StringUtils.isEmpty(caseInfo)){
            caseInfo.setInvoice(InvoiceEnums.PENDING_APPROVAL.getValue());
            caseInfo.setInvoiceBack("");
            caseInfoMapper.updateById(caseInfo);
        }else {
            agreementCustomer.setInvoice(InvoiceEnums.PENDING_APPROVAL.getValue());
            agreementCustomer.setReturnRemarks("");
            customerService.updateById(agreementCustomer);
        }
        if (StringUtils.isEmpty(bean.getId())){
            bean.setIsDel(CommonConstants.ISDEL_NO+"");
            bean.setCreateTime(DateUtil.formatDate(new Date()));
            bean.setPayType("2");
            bean.setCreateUser(user.getId()+"");
            bean.setCaseOrCustomer("1");
            saveOrUpdate(bean);
            if (!StringUtils.isEmpty(caseInfo)){
                approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),caseInfo.getMoney(),user,bean.getId(),null);
            }else{
                approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),agreementCustomer.getServiceFee().toString(),user,bean.getId(),null);
            }
//            for (MultipartFile file : bean.getFiles()) {
//                sysFileService.uploadFile(file, 4, bean.getId());
//            }
//            LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).ne(ProcessApproveRecord::getProcessType,1)
//                    .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
//            List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
//            if (processApproveRecords.size() == 1){//数量为1时  就是最后一个节点通过  案件 发票状态修改为已开票
////                invoiceInfo.setState(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
//                caseInfo.setInvoice(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
//                caseInfoMapper.updateById(caseInfo);
//            }
        }else {
//            LambdaUpdateWrapper<SysFile> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.eq(SysFile::getBizType,4).eq(SysFile::getBizId,bean.getId());
//            SysFile sysFile=new SysFile();
//            sysFile.setIsDel(1);
//            sysFileService.update(sysFile, updateWrapper);
//            for (MultipartFile file : bean.getFiles()) {
//                sysFileService.uploadFile(file,4,bean.getId());
//            }

            if (!StringUtils.isEmpty(caseInfo)){
                LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProcessApproveRecord::getStatus,2).eq(ProcessApproveRecord::getCaseId,bean.getCaseId()).eq(ProcessApproveRecord::getInvoiceInfoId,bean.getId());
                approveRecordService.remove(wrapper);
                approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),caseInfo.getMoney(),user,bean.getId(),null);
            }else{
                LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProcessApproveRecord::getStatus,2).eq(ProcessApproveRecord::getCaseId,bean.getCaseId()).eq(ProcessApproveRecord::getInvoiceInfoId,bean.getId());
                approveRecordService.remove(wrapper);
                approveRecordService.generatorInvoiceRecord(Integer.parseInt(bean.getCaseId()),agreementCustomer.getServiceFee().toString(),user,bean.getId(),null);
            }
            bean.setCreateTime(DateUtil.formatDate(new Date()));
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
        wrapper.eq(SysDict::getDictType,"fileType").eq(SysDict::getMemo,"INVOICE");
        SysDict one = sysDictService.getOne(wrapper);
        InvoiceInfoVo vo = new InvoiceInfoVo();
        BeanUtil.copyProperties(getById(id), vo);
        LambdaQueryWrapper<SysFile> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(SysFile::getBizId,id).in(SysFile::getBizType,one.getDictValue()).eq(SysFile::getIsDel, CommonConstants.ISDEL_NO);
        List<SysFile> list = sysFileService.list(wrapper1);
        vo.setFileList(list);
        if (vo.getCaseOrCustomer().equals("1")){
            CaseInfo caseInfo = caseInfoMapper.selectById(vo.getCaseId());
            vo.setBack(caseInfo.getInvoiceBack());
        }else{
            AgreementCustomer customer = customerService.getById(vo.getCaseId());
            vo.setBack(customer.getReturnRemarks());
        }
        return ResultInfo.success(vo);
    }

    public ResultInfo approveInvoiceInfo(InvoiceInfoVo vo) {
        InvoiceInfo invoiceInfo = getById(vo.getId());
        CaseInfo caseInfo = caseInfoMapper.selectById(invoiceInfo.getCaseId());
        AgreementCustomer customer = customerService.getById(invoiceInfo.getCaseId());
        if (vo.getApproveStatus().equals("1")){//通过
            if (invoiceInfo.getCaseOrCustomer().equals("1")){
                LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).ne(ProcessApproveRecord::getProcessType,1)
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
                ProcessApproveRecord processApproveRecord = processApproveRecords.get(0);
                processApproveRecord.setStatus(1);
                approveRecordService.updateById(processApproveRecord);
                if (processApproveRecords.size() == 1){//数量为1时  就是最后一个节点通过  案件 发票状态修改为已开票
//                invoiceInfo.setState(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                    caseInfo.setInvoice(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                    updateById(invoiceInfo);
                    caseInfo.setInvoiceBack("");
                    caseInfoMapper.updateById(caseInfo);
                }
            }else {
                LambdaQueryWrapper<ProcessApproveRecord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, customer.getId()).ne(ProcessApproveRecord::getProcessType,1)
                        .eq(ProcessApproveRecord::getStatus,0).orderByAsc(ProcessApproveRecord::getSort);
                List<ProcessApproveRecord> processApproveRecords = approveRecordService.list(wrapper);//所有没有审核过的节点
                ProcessApproveRecord processApproveRecord = processApproveRecords.get(0);
                processApproveRecord.setStatus(1);
                approveRecordService.updateById(processApproveRecord);
                if (processApproveRecords.size() == 1){//数量为1时  就是最后一个节点通过  案件 发票状态修改为已开票
//                invoiceInfo.setState(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                    customer.setInvoice(InvoiceEnums.INVOICE_HAS_BEEN_ISSUED.getValue());
                    updateById(invoiceInfo);
                    customer.setReturnRemarks("");
                    customerService.updateById(customer);
                }
            }
        }else {//退回
            if (invoiceInfo.getCaseOrCustomer().equals("1")) {
                ProcessApproveRecord processApproveRecords = new ProcessApproveRecord();
                processApproveRecords.setStatus(2);
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, caseInfo.getId()).ne(ProcessApproveRecord::getProcessType, 1);
                approveRecordService.update(processApproveRecords, wrapper);//把所有节点改成未审批
//            invoiceInfo.setState(InvoiceEnums.RETURNED.getValue());
                caseInfo.setInvoice(InvoiceEnums.RETURNED.getValue());
//            updateById(invoiceInfo);//案件 发票状态修改为退回
                caseInfo.setInvoiceBack(vo.getBack());
                caseInfoMapper.updateById(caseInfo);
            } else {
                ProcessApproveRecord processApproveRecords = new ProcessApproveRecord();
                processApproveRecords.setStatus(2);
                LambdaUpdateWrapper<ProcessApproveRecord> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ProcessApproveRecord::getCaseId, customer.getId()).ne(ProcessApproveRecord::getProcessType, 1);
                approveRecordService.update(processApproveRecords, wrapper);//把所有节点改成未审批
//            invoiceInfo.setState(InvoiceEnums.RETURNED.getValue());
                customer.setInvoice(InvoiceEnums.RETURNED.getValue());
//            updateById(invoiceInfo);//案件 发票状态修改为退回
                customer.setReturnRemarks(vo.getBack());
                customerService.updateById(customer);
            }
        }
        return ResultInfo.success();
    }


    public Boolean toInvoice(Integer id ){
        ProcessDesign entity = processDesignService.toInvoice(id+"");
        return !StringUtils.isEmpty(entity);
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
        List<CaseInfoVo> records = invoiceInfoMapper.selectInvoiceList( vo);
        List<InvoiceInfo> invoiceInfos=new ArrayList<>();
        records.forEach(record -> {
            record.setInvoice(InvoiceEnums.getDescByValue(record.getInvoice()));
            record.setPayType(PayEnums.getDescByValue(record.getPayType()));
           if (!StringUtils.isEmpty(record.getCreateUser())){
                record.setUserName(sysUserService.getById(Integer.parseInt(record.getCreateUser())).getRealName());
            }
            //查询详情
            if (record.getCaseOrCustomer().equals("2")){
                InvoiceInfo invoiceInfo = invoiceInfoMapper.selectById(record.getInvoiceId());
                record.setCust(customerService.detail(Integer.valueOf(invoiceInfo.getCaseId())));
            }
            LambdaQueryWrapper<InvoiceInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InvoiceInfo::getCaseId,record.getCaseId()).eq(InvoiceInfo::getCaseOrCustomer,record.getCaseOrCustomer());
            List<InvoiceInfo> invoiceInfos1 = invoiceInfoMapper.selectList(wrapper);
//            invoiceInfos1.forEach(invoiceInfo -> {
//                LambdaQueryWrapper<SysDict> wrapper1=new LambdaQueryWrapper<>();
//                wrapper1.eq(SysDict::getDictType,"invoiceType").eq(SysDict::getDictValue,invoiceInfo.getInvoiceType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO);
//                SysDict one = sysDictService.getOne(wrapper1);
//                invoiceInfo.setInvoiceType(one.getDictLabel());
//            });
            invoiceInfos.addAll(invoiceInfos1);

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
        writer.addHeaderAlias("content", "发票内容");
        writer.addHeaderAlias("contractMoney", "合同金额");
        writer.addHeaderAlias("invoiceMoney", "开票金额");
        writer.addHeaderAlias("invoiceStr", "发票类型");
        writer.addHeaderAlias("contactPerson", "联系人");
        writer.addHeaderAlias("contactPhone", "联系人电话");
        writer.addHeaderAlias("addressI", "收件地址");
        writer.addHeaderAlias("applyTime", "申请开票时间");
        writer.addHeaderAlias("invoiceTime", "开票时间");
        writer.addHeaderAlias("payType", "发票内容");
        writer.addHeaderAlias("invoiceStr", "审核状态");
//        writer.addHeaderAlias("content", "发票内容");
//        writer.addHeaderAlias("invoiceMoney", "开票金额");
//        writer.addHeaderAlias("contractMoney", "合同金额");
//        writer.addHeaderAlias("invoiceType", "发票类型");
//        writer.addHeaderAlias("contactPerson", "联系人");
//        writer.addHeaderAlias("contactPhone", "联系人电话");
//        writer.addHeaderAlias("mail", "邮箱");
//        writer.addHeaderAlias("address", "收件地址");
//        writer.addHeaderAlias("addressee", "收件人");
//        writer.addHeaderAlias("addresseePhone", "收件人电话");
//        writer.addHeaderAlias("bankAccount", "银行账号");
//        writer.addHeaderAlias("bankDeposit", "开户行");
//        writer.addHeaderAlias("registerTel", "注册电话");
//        writer.addHeaderAlias("registeredAdd", "注册地址");
//        writer.addHeaderAlias("number", "单位税号");
//        writer.addHeaderAlias("title", "抬头");
        writer.setOnlyAlias(true);
        writer.write(records, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("InvoiceInfo.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
//            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void payType(InvoiceInfoVo bean) {
       updateById(bean);
    }

}


