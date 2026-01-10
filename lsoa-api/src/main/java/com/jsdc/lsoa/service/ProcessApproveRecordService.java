package com.jsdc.lsoa.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.mapper.ProcessApproveRecordMapper;
import com.jsdc.lsoa.mapper.ProcessDesignMapper;
import com.jsdc.lsoa.mapper.ProcessNodeMapper;
import com.jsdc.lsoa.mapper.SysDictMapper;
import com.jsdc.lsoa.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProcessApproveRecordService extends BaseService<ProcessApproveRecord> {

    @Autowired
    private ProcessApproveRecordMapper recordMapper;

    @Autowired
    private ProcessDesignMapper designMapper;

    @Autowired
    private ProcessNodeMapper nodeMapper;

    @Autowired
    private SysDictMapper sysDictMapper;


    /**
     * 开票审核记录生成
     * @param caseId 案件ID
     * @param amount 开票金额
     * @param loginUser 登录用户
     */
    public void generatorInvoiceRecord(Integer caseId, String amount, SysUser loginUser, Integer invoiceInfoId,Integer type) {
        Date now = new Date();
        SysDict dict = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>().eq(SysDict::getIsDel, CommonConstants.ISDEL_NO).eq(SysDict::getDictType, CommonConstants.INVOICE_PROCESS_AMOUNT_KEY));
        // processType 流程类型 1：开函，2：开票（金额小于配置金额），3：开票（金额大于配置金额）4 顾问客户开票
        // 发票金额 > 配置金额
        ProcessDesign design;
        if (new BigDecimal(amount).compareTo(new BigDecimal(dict.getDictValue())) < 0) {
            design = designMapper.selectOne(new LambdaQueryWrapper<ProcessDesign>().eq(ProcessDesign::getProcessType, 2));
        }else {
            design = designMapper.selectOne(new LambdaQueryWrapper<ProcessDesign>().eq(ProcessDesign::getProcessType, 3));
        }
        List<ProcessNode> nodeList = nodeMapper.selectList(new LambdaQueryWrapper<ProcessNode>()
                .eq(ProcessNode::getProcessId, design.getId())
                .orderByAsc(ProcessNode::getSort)
        );
        int sort = 1;
        for (ProcessNode node : nodeList) {
            ProcessApproveRecord record = new ProcessApproveRecord();
            record.setProcessId(design.getId());
            record.setProcessType(null == type ?design.getProcessType():type);
            record.setProcessNodeId(node.getId());
            record.setNodeType(node.getNodeType());
            record.setCaseId(caseId);
            record.setApproverType(node.getApproverType());
            record.setApproverValue(node.getApproverValue());
            record.setSort(sort);
            if (node.getNodeType() == 1) {
                record.setStatus(0);
            }else {
                record.setStatus(1);
            }
            record.setInvoiceInfoId(invoiceInfoId);
            record.setCreateUser(loginUser.getId());
            record.setUpdateUser(loginUser.getId());
            record.setCreateTime(now);
            record.setUpdateTime(now);
            recordMapper.insert(record);
            sort++;
        }
    }

    /**
     * 开函审核记录生成
     * @param caseId 案件ID
     * @param loginUser 登录用户
     */
    public void generatorLetterRecord(Integer caseId, SysUser loginUser, Integer summonsInfoId) {
        Date now = new Date();
        ProcessDesign design = designMapper.selectOne(new LambdaQueryWrapper<ProcessDesign>().eq(ProcessDesign::getProcessType, 1));
        List<ProcessNode> nodeList = nodeMapper.selectList(new LambdaQueryWrapper<ProcessNode>()
                .eq(ProcessNode::getProcessId, design.getId())
                .orderByAsc(ProcessNode::getSort)
        );
        int sort = 1;
        for (ProcessNode node : nodeList) {
            ProcessApproveRecord record = new ProcessApproveRecord();
            record.setProcessId(design.getId());
            record.setProcessType(design.getProcessType());
            record.setProcessNodeId(node.getId());
            record.setNodeType(node.getNodeType());
            record.setCaseId(caseId);
            record.setApproverType(node.getApproverType());
            record.setApproverValue(node.getApproverValue());
            record.setSort(sort);
            if (node.getNodeType() == 1) {
                record.setStatus(0);
            }else {
                record.setStatus(1);
            }
            record.setSummonsInfoId(summonsInfoId);
            record.setCreateUser(loginUser.getId());
            record.setUpdateUser(loginUser.getId());
            record.setCreateTime(now);
            record.setUpdateTime(now);
            recordMapper.insert(record);
            sort++;
        }
    }


}
