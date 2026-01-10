package com.jsdc.lsoa.dao;
import cn.hutool.core.util.StrUtil;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.InvoiceInfo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class InvoiceInfoDao extends BaseDao<InvoiceInfo>  {



    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param
     * @param bean
     * @return
     */
    public String getEntityList(CaseInfoVo bean) {
        String sql = " SELECT *  "+
                " FROM invoice_info  "+
                " WHERE "+
                " 1=1 AND isdel=0 AND caseId="+bean.getId();
        sql+=" ORDER BY createTime DESC";
        return sql;
    }

    public String  selectInvoicePage(Page page, CaseInfoVo vo) {
        StringBuilder sql = new StringBuilder(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
        sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=0  AND a.nodeType=1 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId \n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCaseId())){
            sql.append("AND b.caseId = '").append(vo.getCaseId()).append("' ");
            sql.append("AND b.caseOrCustomer = 2");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='"+vo.getUserName()+"')");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCustomerId())){
            sql.append(" AND a.customerId = '").append(vo.getCustomerId()).append("' ");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        sql.append(" UNION all  " );
        sql.append(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
                sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=1  AND a.nodeType=1 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId \n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCaseId())){
            sql.append("AND b.caseId = '").append(vo.getCaseId()).append("' ");
            sql.append("AND b.caseOrCustomer = 2");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='"+vo.getUserName()+"')");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCustomerId())){
            sql.append(" AND a.customerId = '").append(vo.getCustomerId()).append("' ");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        sql.append(" UNION all  " );
        sql.append(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
                sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=2  AND a.nodeType=1 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId \n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCaseId())){
            sql.append("AND b.caseId = '").append(vo.getCaseId()).append("' ");
            sql.append("AND b.caseOrCustomer = 2");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='").append(vo.getUserName()).append("') ");
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCustomerId())){
            sql.append(" AND a.customerId = '").append(vo.getCustomerId()).append("' ");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        return sql.toString();
    }






    public String  selectInvoiceList( CaseInfoVo vo) {
        StringBuilder sql = new StringBuilder(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
//                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
//                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tRIGHT JOIN (SELECT * FROM invoice_info a ) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
                //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
                sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
//                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=0 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId\n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=0 ORDER BY sort ) a ) c ON b.id = c.invoiceInfoId\n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='"+vo.getUserName()+"')");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        sql.append(" UNION all  " );
        sql.append(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
//                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
//                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tRIGHT JOIN (SELECT * FROM invoice_info a ) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
                sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
//                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=1 AND a.nodeType=1 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId \n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=1 AND a.nodeType=1 ORDER BY sort ) a ) c ON b.id = c.invoiceInfoId \n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='"+vo.getUserName()+"')");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        sql.append(" UNION all  " );
        sql.append(" (SELECT\n" +
                "\ta.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tinvoice_Type.dictLabel as invoiceStr,\n" +
                "\tb.id as invoiceId,\n" +
                "\tb.content,\n" +
                "\tb.mail,\n" +
                "\tb.invoiceMoney,\n" +
                "\tb.contractMoney,\n" +
                "\tb.createTime  as applyTime,\n" +
                "\tb.invoiceType,\n" +
                "\tb.address as addressI,\n" +
                "\tb.contactPerson,\n" +
                "\tb.contactPhone,\n" +
                "\tb.invoiceTime,\n" +
                "\tb.caseOrCustomer,\n" +
                "\tb.caseId,\n" +
//                "b.invoiceNum," +
                "b.payType," +
                "\t\tIFNULL(b.state,0) state\n" +
                "FROM\n" +
                "\tcase_info a\n" +
//                "\tRIGHT JOIN (SELECT *,COUNT(1) invoiceNum FROM invoice_info a GROUP BY a.caseId,a.caseOrCustomer) b ON a.id = b.caseId\n" +
                "\tRIGHT JOIN (SELECT * FROM invoice_info a ) b ON a.id = b.caseId\n" +
                "\tLEFT JOIN agreement_customer d ON d.id=b.caseId " );
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
            sql.append("LEFT JOIN (SELECT * FROM customer_lawyers WHERE lawyerId=").append(vo.getUserName()).append(") e ON e.customerId=b.caseId");
        }
                sql.append("\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = a.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict invoice_Type ON invoice_Type.dictValue = b.invoiceType  \tAND invoice_Type.dictType = 'invoiceType'\n" +
//                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=2  AND a.nodeType=1 ORDER BY sort ) a GROUP BY a.caseId ) c ON b.id = c.invoiceInfoId \n" +
                "\tLEFT JOIN ( SELECT * FROM ( SELECT * FROM process_approve_record a where a.processType!=1 AND a.`status`=2  AND a.nodeType=1 ORDER BY sort ) a ) c ON b.id = c.invoiceInfoId \n" +
                "WHERE\n" +
                "\t1 = 1   " +
                "and b.isdel=0  AND c.id is NOT NULL ");
        // 案件编号/名称
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql.append(" AND a.caseNum LIKE '%").append(vo.getCaseNum()).append("%' ");
        }
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql.append(" AND a.caseName LIKE '%").append(vo.getCaseName()).append("%' ");
        }
        //        // 委托人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql.append(" AND a.client LIKE '%").append(vo.getClient()).append("%' ");
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql.append("AND a.caseType = '").append(vo.getCaseType()).append("' ");
        }
//        //所属部门
//        if (StrUtil.isNotBlank(vo.getDeptId())){
//            sql.append("AND a.deptid = '").append(vo.getDeptId()).append("'");
//        }
        // 发起人--审批人
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql.append("AND ( a.createUser = '").append(vo.getCreateUser()).append("'");
            if (StrUtil.isNotBlank(vo.getApproveUser())){
                sql.append("\n OR ");
                sql.append(" ((\n" + "\t\tFIND_IN_SET( '").append(vo.getApproveUser()).append("', c.approverValue )  AND c.approverType=1)\n");
                if(!StringUtils.isEmpty(vo.getApproveRole())){
                    for (SysRole sysRole : vo.getApproveRole()) {
                        sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', c.approverValue ) AND c.approverType=2)\n");
                    }
                }
                sql.append(")\n");
            }
            sql.append(") ");
        }
        //承办律师
        if (StrUtil.isNotBlank(vo.getUserName())) {
           sql.append("AND ( a.createUser = '").append(vo.getUserName()).append("' or  e.lawyerId='"+vo.getUserName()+"')");
        }
        // 是否开票
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql.append("AND (a.invoice in (").append(vo.getInvoice()).append(") or  d.invoice IN (").append(vo.getInvoice()).append("))");
        }
        // 是否开函
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql.append("AND a.letter = '").append(vo.getLetter()).append("' ");
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql.append(" AND b.invoiceTime BETWEEN '"+star+"' AND '"+end+"'  ");
        }
        sql.append(" order by   case a.invoice when '2' then 1 when '1' then 2 when '3' then 4 when '4' then 3 end asc)");
        return sql.toString();
    }
}
