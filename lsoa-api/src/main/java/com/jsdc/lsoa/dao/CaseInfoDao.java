package com.jsdc.lsoa.dao;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.CaseInfo;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.StatisticalVo;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class CaseInfoDao extends BaseDao<CaseInfo>  {




    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param page
     * @param bean
     * @return
     */
    public String getEntityList(Page page, CaseInfo bean) {
        String sql = " SELECT *  "+
                " FROM case_info  "+
                " WHERE "+ 
                " 1=1 ";

        return sql;
    }

    public String selectPageWhere(Page page, CaseInfoVo vo) {
        String sql = " SELECT\n" +
                "\tci.*,\n" +
                "\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tsd_businessType.dictLabel as businessTypeStr,\n" +
                "\tsd_stage.dictLabel as stageStr,\n" +
                "\tsu.realName as userName, \n" +
                "\tii.id as invoiceId \n" +
//                "\tsi.id as summonsId \n" +
                "FROM\n" +
                "\tcase_info ci \n" +
                "\tLEFT JOIN sys_user su on su.id = ci.createUser  AND su.isDel = 0\n" +
                // 关联字典
                "\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = ci.caseType AND sd_caseType.dictType = 'caseType' \n" +
                "\tLEFT JOIN sys_dict sd_businessType ON sd_businessType.dictValue = ci.businessType AND sd_businessType.dictType = 'businessType' \n" +
                "\tLEFT JOIN sys_dict sd_stage ON sd_stage.dictValue = ci.stage AND sd_stage.dictType = 'litigationStage' \n" +
                "\tLEFT JOIN (select * from invoice_info group by caseId) ii ON ii.caseId = ci.id AND ii.isDel = 0  \n" +
//                "\tLEFT JOIN summons_info si ON si.caseId = ci.id AND si.isDel = 0 \n" +
                "WHERE\n" +
                "\t1 = 1 AND ci.isDel = 0 ";
        // 案件号
        if (StrUtil.isNotBlank(vo.getCaseNum())){
            sql+=" AND ci.caseNum LIKE '%"+vo.getCaseNum()+"%' ";
        }
        // 案件名称
        if (StrUtil.isNotBlank(vo.getCaseName())){
            sql+=" AND ci.caseName LIKE '%"+vo.getCaseName()+"%' ";
        }
        // 当事人
        if (StrUtil.isNotBlank(vo.getClient())){
            sql+=" AND ci.client LIKE '%"+vo.getClient()+"%' ";
        }
        // 案件类型
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql+=" AND ci.caseType = '"+vo.getCaseType()+"' ";
        }
        // 业务类型
        if (StrUtil.isNotBlank(vo.getBusinessType())){
            sql+=" AND ci.businessType = '"+vo.getBusinessType()+"' ";
        }
        // 案件状态
        if (StrUtil.isNotBlank(vo.getStatus())){
            sql+=" AND ci.status = '"+vo.getStatus()+"' ";
        }
        // 案件状态 多选
        if (StrUtil.isNotBlank(vo.getStatusList())){
            sql+=" AND ci.status in ("+vo.getStatusList()+") ";
        }
        // 诉讼阶段
        if (StrUtil.isNotBlank(vo.getStage())){
            sql+=" AND ci.stage = '"+vo.getStage()+"' ";
        }
        // 登记时间
        if (StrUtil.isNotBlank(vo.getStartTime())&&StrUtil.isNotBlank(vo.getEndTime())){
            String star = vo.getStartTime()+" 00:00:00";
            String end = vo.getEndTime()+" 23:59:59";
            sql+=" AND ci.createTime BETWEEN '"+star+"' AND '"+end+"'  ";
        }
        // 开票状态
        if (StrUtil.isNotBlank(vo.getInvoice())){
            sql+=" AND ci.invoice = '"+vo.getInvoice()+"' ";
        }
        // 开函状态
        if (StrUtil.isNotBlank(vo.getLetter())){
            sql+=" AND ci.letter = '"+vo.getLetter()+"' ";
        }
        // 归档状态
        if (StrUtil.isNotBlank(vo.getArchived())){
            sql+=" AND ci.archived = '"+vo.getArchived()+"' ";
        }
        // 本人案件
        if (StrUtil.isNotBlank(vo.getCreateUser())){
            sql+=" AND ci.createUser = '"+vo.getCreateUser()+"' ";
        }
        // 本科室案件
        if (StrUtil.isNotBlank(vo.getDeptId())){
            sql+=" AND ci.deptId = '"+vo.getDeptId()+"' ";
        }
        // 案由
        if (StrUtil.isNotBlank(vo.getSubjectOfAction())){
            sql+=" AND ci.subjectOfAction like '%"+vo.getSubjectOfAction()+"%' ";
        }
        // 顾问单位
        if (StrUtil.isNotBlank(vo.getCustomerId())){
            sql+=" AND ci.customerId = '"+vo.getCustomerId()+"' ";
        }
        sql+=" Order By CASE WHEN ci.createUser  = "+vo.getCurrentUserId()+" THEN 0 ELSE 1  END ASC, " +
                // 当前用户的订单按自定义状态排序
                "    CASE \n" +
                "        WHEN ci.createUser  = "+vo.getCurrentUserId()+" THEN \n" +
                "            CASE \n" +
                "                WHEN ci.invoice  = '1' THEN 1 \n" +
                "                WHEN ci.invoice  = '4' THEN 2 \n" +
                "                ELSE 3 \n" +
                "            END \n" +
                "        ELSE NULL \n" +
                "    END ASC,\n" +
                "    CASE \n" +
                "        WHEN ci.createUser  = "+vo.getCurrentUserId()+" THEN \n" +
                "            CASE \n" +
                "                WHEN ci.letter  = '1' THEN 1 \n" +
                "                WHEN ci.letter  = '4' THEN 2 \n" +
                "                ELSE 3 \n" +
                "            END \n" +
                "        ELSE NULL \n" +
                "    END ASC,\n" +
                // 其他用户的订单按 id 降序排列
                "    CASE \n" +
                "        WHEN ci.createUser  != "+vo.getCurrentUserId()+" THEN ci.createTime  \n" +
                "        ELSE NULL \n" +
                "    END DESC ,ci.createTime DESC" ;

        return sql;
    }

    public String CaseTypeRanking(StatisticalVo vo,String type){
        String sql = "SELECT\n" +
                "\td.dictLabel AS caseTypeName,\n" ;
        if (Objects.equals(type,"count")){
            sql +="\tCOUNT( c.caseType ) AS caseCount \n";
        }else {
            sql +=" SUM( CASE WHEN c.money IS NOT NULL THEN CAST( c.money AS DECIMAL ( 65, 2 )) ELSE 0 END ) AS totalMoney \n";
        }
        sql +=  "FROM\n" +
                "\tsys_dict d\n" +
                "\tLEFT JOIN case_info c ON d.dictValue = c.caseType \n" +
                "\tAND c.isDel = 0 \n" ;

        if (StrUtil.isNotBlank(vo.getUserId())) {
            sql += " AND c.createUser = '" + vo.getUserId() + "' ";
        }
//        if (StrUtil.isNotBlank(vo.getCaseType())){
//            sql+=" AND c.caseType = '"+vo.getCaseType()+"' ";
//        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
        if (Objects.equals(vo.getFlag(),"year")){
//            Date beginOfYear = DateUtil.beginOfYear(date);
//            String start = sdf.format(beginOfYear);
//            Date endOfYear = DateUtil.endOfYear(date);
//            String end = sdf.format(endOfYear);
            String start = vo.getTime()+"-01-01 00:00:00";
            String end = vo.getTime()+"-12-31 23:59:59";
            sql += " AND c.createTime BETWEEN '"+start+"' AND '"+end+"'  ";
        }else if (Objects.equals(vo.getFlag(),"month")){
            // 将字符串解析为日期对象
            Date date = DateUtil.parse(vo.getTime()+"-01");
            // 获取该月的月初时间
            Date beginOfMonth = DateUtil.beginOfMonth(date);
            // 获取该月的月末时间
            Date endOfMonth = DateUtil.endOfMonth(date);
            // 格式化输出结果
            String start = DateUtil.format(beginOfMonth, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss");
            sql += " AND c.createTime BETWEEN '"+start+"' AND '"+end+"'  ";
        }else if (Objects.equals(vo.getFlag(),"day") && StrUtil.isNotBlank(vo.getStartTime())&& StrUtil.isNotBlank(vo.getEndTime())){
            Date date1 = DateUtil.parse(vo.getStartTime()+"-01");
            // 获取该月的月初时间
            Date beginOfMonth = DateUtil.beginOfMonth(date1);

            Date date2 = DateUtil.parse(vo.getEndTime()+"-01");
            // 获取该月的月初时间
            Date endOfMonth = DateUtil.endOfMonth(date2);
            String start = DateUtil.format(beginOfMonth, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss");

            sql += " AND c.createTime BETWEEN '"+start+"' AND '"+end+"'  ";
        }

        sql+=   "WHERE\n" +
                "\t1 = 1 \n" +
                "\tAND d.dictType = 'caseType' \n" +
                "\tAND d.isDel = 0 \n" ;
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql+=" AND d.dictValue = '"+vo.getCaseType()+"' ";
        }
        sql+=   "GROUP BY\n" +
                "\td.dictLabel \n" +
                "ORDER BY\n" +
                "\t "+(Objects.equals(type,"count")? "caseCount" : "totalMoney")+" DESC;";
        return sql;
    }

    public String statByDay(StatisticalVo vo,String type){
        String sql = "SELECT SUBSTRING(createTime, 1, 7) AS time, " ;// 按整月统计数 7  按天为10
        if (Objects.equals(type,"count")){
            sql +="\tCOUNT(id) AS caseCount \n";
        }else {
            sql +=" SUM( CASE WHEN money IS NOT NULL THEN CAST( money AS DECIMAL ( 65, 2 )) ELSE 0 END ) AS totalMoney \n";
        }
        // 解析开始时间和结束时间
        Date startTime = DateUtil.parse(vo.getStartTime() + "-01");
        Date endTime = DateUtil.parse(vo.getEndTime() + "-01");

        // 获取开始时间的月头
        Date startOfMonth = DateUtil.beginOfMonth(startTime);

        // 获取结束时间的月尾
        Date endOfMonth = DateUtil.endOfMonth(endTime);

        // 格式化输出结果
        String startOfMonthStr = DateUtil.format(startOfMonth, "yyyy-MM-dd HH:mm:ss");
        String endOfMonthStr = DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss");
        sql +=  "            FROM case_info \n" +
                "            WHERE 1=1 AND isDel = 0 AND createTime >= '"+startOfMonthStr+"' AND createTime <= '"+endOfMonthStr+"'\n";
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql+=" AND caseType = '"+vo.getCaseType()+"' ";
        }
        if (StrUtil.isNotBlank(vo.getUserId())){
            sql+=" AND createUser = '"+vo.getUserId()+"' ";
        }
        sql += "            GROUP BY SUBSTRING(createTime, 1, 7)";// 按整月统计数 7  按天为10
        return sql;
    }

    public String statByDayOfMonth(StatisticalVo vo,String type){
        String sql = "SELECT SUBSTRING(createTime, 1, 7) AS time," ;// 按整月统计数 7  按天为10
        if (Objects.equals(type,"count")){
            sql +="\tCOUNT(id) AS caseCount \n";
        }else {
            sql +=" SUM( CASE WHEN money IS NOT NULL THEN CAST( money AS DECIMAL ( 65, 2 )) ELSE 0 END ) AS totalMoney \n";
        }
        sql +=  "            FROM case_info \n" +
                "           WHERE 1=1 AND isDel = 0 AND  SUBSTRING(createTime, 1, 7) = '"+vo.getTime()+"' \n";
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql+=" AND caseType = '"+vo.getCaseType()+"' ";
        }
        if (StrUtil.isNotBlank(vo.getUserId())){
            sql+="        AND createUser = '"+vo.getUserId()+"' ";
        }
        sql += "        GROUP BY SUBSTRING(createTime, 1, 7)"; // 按整月统计数 7  按天为10
        return sql;
    }

    public String statByMonthOfYear(StatisticalVo vo,String type){
        String sql = "SELECT SUBSTRING(createTime, 1, 7) AS time, " ;
        if (Objects.equals(type,"count")){
            sql +="\tCOUNT(id) AS caseCount \n";
        }else {
            sql +=" SUM( CASE WHEN money IS NOT NULL THEN CAST( money AS DECIMAL ( 65, 2 )) ELSE 0 END ) AS totalMoney \n";
        }
        sql +=  "            FROM case_info \n" +
                "           WHERE 1=1 AND isDel = 0 AND  SUBSTRING(createTime, 1, 4) = '"+vo.getTime()+"' \n";
        if (StrUtil.isNotBlank(vo.getCaseType())){
            sql+=" AND caseType = '"+vo.getCaseType()+"' ";
        }
        if (StrUtil.isNotBlank(vo.getUserId())){
            sql+=" AND createUser = '"+vo.getUserId()+"' ";
        }
        sql += " GROUP BY SUBSTRING(createTime, 1, 7)";
        return sql;
    }


    public String getCaseStatistics(StatisticalVo vo,List<SysDict> caseTypes,String type) {

        // 动态生成 SUM(CASE WHEN ...) 部分
        StringBuilder caseSumBuilder = new StringBuilder();
        if (Objects.equals(type,"count")){
            caseSumBuilder.append(" COUNT(c.caseType) AS totalCases, ");
            for (SysDict caseType : caseTypes) {
                caseSumBuilder.append(" SUM(CASE WHEN c.caseType = '").append(caseType.getDictValue()).append("'  THEN 1 ELSE 0 END) AS ")
                        .append(" '").append(caseType.getDictValue()).append("', ");
            }
        }else {
            caseSumBuilder.append(" SUM(CASE WHEN c.money IS NOT NULL THEN CAST(c.money AS DECIMAL(65,2)) ELSE 0 END) AS totalMoney, ");
            for (SysDict caseType : caseTypes) {
                caseSumBuilder.append(" SUM(CASE WHEN c.caseType = '").append(caseType.getDictValue()).append("' AND c.money IS NOT NULL THEN CAST(c.money AS DECIMAL(65,2)) ELSE 0 END) AS ")
                        .append(" '").append(caseType.getDictValue()).append("', ");
            }
        }
        // 移除最后的逗号
        String caseSumQuery = caseSumBuilder.toString();
        if (caseSumQuery.endsWith(", ")) {
            caseSumQuery = caseSumQuery.substring(0, caseSumQuery.length() - 2);
        }

        // 构建 SQL 查询
        String sql = "";
        if (Objects.equals(vo.getFlag(),"day")) {
            Date date1 = DateUtil.parse(vo.getStartTime()+"-01");
            // 获取该月的月初时间
            Date beginOfMonth = DateUtil.beginOfMonth(date1);

            Date date2 = DateUtil.parse(vo.getEndTime()+"-01");
            // 获取该月的月初时间
            Date endOfMonth = DateUtil.endOfMonth(date2);
            String start = DateUtil.format(beginOfMonth, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss");
            // 时间段查询
            sql = "SELECT DATE_FORMAT(c.createTime, '%Y-%m' ) AS time,  " +
                    caseSumQuery +
                    " FROM  case_info c " +
                    " WHERE 1=1 AND c.isDel = 0 AND  c.createTime BETWEEN '"+start+"' AND '"+end+"' " ;
            if (StrUtil.isNotBlank(vo.getCaseType())){
                sql +=" AND  caseType = '"+vo.getCaseType()+"' ";
            }
            if (StrUtil.isNotBlank(vo.getUserId())){
                sql+=" AND createUser = '"+vo.getUserId()+"' ";
            }
            sql +=" GROUP BY DATE_FORMAT(c.createTime, '%Y-%m' ) " +
                    " ORDER BY time;";
        } else if (Objects.equals(vo.getFlag(),"month")) {
            // 按月份查询
            sql = "SELECT DATE_FORMAT( c.createTime, '%Y-%m' ) AS time, " +
                    caseSumQuery +
                    " FROM  case_info c " +
                    " WHERE  1=1 AND c.isDel = 0 AND  DATE_FORMAT( c.createTime, '%Y-%m' ) = '"+vo.getTime()+"' ";
            if (StrUtil.isNotBlank(vo.getCaseType())){
                sql +=" AND  caseType = '"+vo.getCaseType()+"' ";
            }
            if (StrUtil.isNotBlank(vo.getUserId())){
                sql+=" AND createUser = '"+vo.getUserId()+"' ";
            }
            sql += " GROUP BY DATE_FORMAT( c.createTime, '%Y-%m' )  ORDER BY time";
        } else if (Objects.equals(vo.getFlag(),"year")) {
            // 按年份查询
            sql = "SELECT DATE_FORMAT( c.createTime, '%Y-%m' )  AS time, " +
                    caseSumQuery +
                    " FROM case_info c " +
                    " WHERE  1=1 AND c.isDel = 0 AND  YEAR(c.createTime) = '"+vo.getTime()+"' " ;
            if (StrUtil.isNotBlank(vo.getCaseType())){
                sql +=" AND  caseType = '"+vo.getCaseType()+"' ";
            }
            if (StrUtil.isNotBlank(vo.getUserId())){
                sql+=" AND createUser = '"+vo.getUserId()+"' ";
            }
            sql +=" GROUP BY DATE_FORMAT( c.createTime, '%Y-%m' )  ORDER BY time";
        }

        return sql;
    }


    public String toDo(Page page, CaseInfoVo vo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tb.*,\n" +
                "\ta.processType,\n" +
                "\tii.caseId," +
                "\t\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tii.id AS invoiceId,\n" +
                "\tsi.id AS summonsId,\n" +
                "\tii.invoiceTime,\n" +
                "\tsi.summonsTime,\n" +
                "\tii.caseOrCustomer\n" +
                "FROM\n" +
                "case_info b RIGHT JOIN\n"+
                "\t(\n" +
                "\tSELECT\n" +
                "\t\t* \n" +
                "\tFROM\n" +
                "\t\t( SELECT * FROM process_approve_record a WHERE a.`status` = 0  ORDER BY sort ) a \n" +
                "\tGROUP BY\n" +
                "\t\ta.caseId,\n" +
                "\t\ta.processType \n" +
                "\t) a ON a.caseId=b.id\n" +
                "\tLEFT JOIN invoice_info ii ON ii.id = a.invoiceInfoId \n" +
                "\tAND ii.isDel = 0\n" +
                "\tLEFT JOIN summons_info si ON si.id = a.summonsInfoId \n" +
                "\tAND si.isDel = 0 " +
                "\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = b.caseType AND sd_caseType.dictType = 'caseType' " +
                "WHERE\n" +
                "\t1 = 1 ");
        if (!StringUtils.isEmpty(vo.getApproveType())){
            if (vo.getApproveType().equals("1")){
                sql.append(" AND ( a.processType = 1 ) ");
            }else if (vo.getApproveType().equals("2")){
                sql.append(" AND ( a.processType != 1 ) ");
            }
        }
        if (!StringUtils.isEmpty(vo.getApproveUser())){
            sql.append(" and ((\n FIND_IN_SET( '").append(vo.getApproveUser()).append("', a.approverValue ) AND a.approverType=1)\n");
            if(!StringUtils.isEmpty(vo.getApproveRole())){
                for (SysRole sysRole : vo.getApproveRole()) {
                    sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', a.approverValue ) AND a.approverType=2)\n");
                }
            }
            sql.append(")\n");
        }
        if (!StringUtils.isEmpty(vo.getOrder())){
            if (vo.getOrder().equals("1")){
                sql.append(" order by a.createTime asc ");
            }else{
                sql.append(" order by a.createTime desc ");
            }
        }

        return sql.toString();
    }


    public String finish(Page page, CaseInfoVo vo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tb.*,\n" +
                "\ta.processType,\n" +
                "\tii.caseId," +
                "\t\tsd_caseType.dictLabel as caseTypeStr,\n" +
                "\tii.id AS invoiceId,\n" +
                "\tsi.id AS summonsId,\n" +
                "\tii.invoiceTime,\n" +
                "\tsi.summonsTime,\n" +
                "\tii.caseOrCustomer\n" +
                "FROM\n" +
                " case_info b RIGHT JOIN " +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\t* \n" +
                "\tFROM\n" +
                "\t\t( SELECT * FROM process_approve_record a WHERE a.`status` != 0  AND a.nodeType=1  ORDER BY sort ) a \n" +
                "\tGROUP BY\n" +
                "\t\ta.caseId,\n" +
                "\t\ta.processType \n" +
                "\t) a  ON a.caseId=b.id\n" +
                "\tLEFT JOIN invoice_info ii ON ii.id = a.invoiceInfoId \n" +
                "\tAND ii.isDel = 0\n" +
                "\tLEFT JOIN summons_info si ON si.id = a.summonsInfoId \n" +
                "\tAND si.isDel = 0 " +
                "\tLEFT JOIN sys_dict sd_caseType ON sd_caseType.dictValue = b.caseType AND sd_caseType.dictType = 'caseType' \t" +
                "WHERE\n" +
                "\t1 = 1 " );
        if (!StringUtils.isEmpty(vo.getApproveType())){
            if (vo.getApproveType().equals("1")){
                sql.append(" AND ( a.processType = 1 ) ");
            }else if (vo.getApproveType().equals("2")){
                sql.append(" AND ( a.processType != 1 ) ");
            }
        }
        if (!StringUtils.isEmpty(vo.getApproveUser())){
            sql.append(" and ((\n FIND_IN_SET( '").append(vo.getApproveUser()).append("', a.approverValue ) AND a.approverType=1)\n");
            if(!StringUtils.isEmpty(vo.getApproveRole())){
                for (SysRole sysRole : vo.getApproveRole()) {
                    sql.append("\t\t OR (FIND_IN_SET( '").append(sysRole.getId()).append("', a.approverValue ) AND a.approverType=2)\n");
                }
            }
            sql.append(")\n");
        }
        if (!StringUtils.isEmpty(vo.getOrder())){
            if (vo.getOrder().equals("1")){
                sql.append(" order by a.createTime asc ");
            }else{
                sql.append(" order by a.createTime desc ");
            }
        }

        return sql.toString();
    }

}
