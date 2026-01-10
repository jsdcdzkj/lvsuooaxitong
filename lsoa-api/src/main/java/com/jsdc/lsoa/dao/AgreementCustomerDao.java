package com.jsdc.lsoa.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.AgreementCustomer;
import com.jsdc.lsoa.model.CaseInfo;
import com.jsdc.lsoa.vo.AgreementCustomerVO;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class AgreementCustomerDao extends BaseDao<AgreementCustomer> {

    public String getPage(Page page, AgreementCustomerVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ac.*,");
        sql.append(" group_concat(cl.lawyerId separator ',') as lawyerStr");
        sql.append(" from agreement_customer ac left join customer_lawyers cl on ac.id = cl.customerId");
        sql.append(" where ac.isDel = 0");
        if (Objects.nonNull(vo.getInService())) {
            sql.append(" and ac.inService = ").append(vo.getInService());
        }
        if (StrUtil.isNotBlank(vo.getCreditCode())) {
            sql.append(" and ac.creditCode LIKE ('%").append(vo.getCreditCode()).append("%')");
        }
        if (StrUtil.isNotBlank(vo.getCustomerName())) {
            sql.append(" and ac.customerName LIKE '%").append(vo.getCustomerName()).append("%'");
        }
        if (StrUtil.isNotBlank(vo.getCustomerCode())) {
            sql.append(" and ac.customerCode LIKE '%").append(vo.getCustomerCode()).append("%'");
        }
        if (Objects.nonNull(vo.getFeeStart())) {
            sql.append(" and ac.serviceFee >= ").append(vo.getFeeStart());
        }
        if (Objects.nonNull(vo.getFeeEnd())) {
            sql.append(" and ac.serviceFee <= ").append(vo.getFeeEnd());
        }
        sql.append(" group by ac.id");
        if (Objects.nonNull(vo.getUserId()) || Objects.nonNull(vo.getLawyer())) {
            sql.append(" having");
            if (Objects.nonNull(vo.getUserId())) {
                sql.append(" find_in_set('").append(vo.getUserId()).append("', lawyerStr) > 0");
            }
            if (Objects.nonNull(vo.getUserId()) && Objects.nonNull(vo.getLawyer())) {
                sql.append(" and find_in_set('").append(vo.getLawyer()).append("', lawyerStr) > 0");
            }
            else if (Objects.nonNull(vo.getLawyer())) {
                sql.append(" find_in_set('").append(vo.getLawyer()).append("', lawyerStr) > 0");
            }
        }
        sql.append(" order by ac.createTime desc");
        return sql.toString();
    }


    public String getEntityList(Page page, AgreementCustomerVO vo ) {
        String sql = "SELECT\n" +
                "\tac.* \n" +
                "FROM\n" +
                "\tagreement_customer ac\n" +
                "\tLEFT JOIN (select * from customer_lawyers group by customerId) cl ON ac.id = cl.customerId \n" +
                "WHERE\n" +
                "\t1 = 1 \n" +
                "\tAND ac.isDel = 0 \n" +
                "\tAND ac.parentId = 0 \n" ;
        if (Objects.nonNull(vo.getUserId())) {
            sql+= "\tAND cl.lawyerId = "+vo.getUserId()+" \n";
        }
        if (Objects.nonNull(vo.getInService())){
            sql += "\tAND ac.inService = "+vo.getInService()+" \n";
        }
        if (StrUtil.isNotBlank(vo.getCreditCode())){
            sql += "\tAND ac.creditCode LIKE '%"+vo.getCreditCode()+"%' \n";
        }
        if (StrUtil.isNotBlank(vo.getCustomerName())){
            sql += "\tAND ac.customerName LIKE '%"+vo.getCustomerName()+"%' \n";
        }
        if (StrUtil.isNotBlank(vo.getCustomerCode())){
            sql += "\tAND ac.customerCode LIKE '%"+vo.getCustomerCode()+"%' \n";
        }
        if (Objects.nonNull(vo.getFeeStart())){
            sql += "\tAND ac.serviceFee >= "+vo.getFeeStart()+" \n";
        }
        if (Objects.nonNull(vo.getFeeEnd())){
            sql += "\tAND ac.serviceFee <= "+vo.getFeeEnd()+" \n";
        }
        sql+="ORDER BY ac.createTime DESC";
        return sql;
    }

    public String getEntityListByUser(Page page, AgreementCustomerVO vo ) {
        String sql = "SELECT\n" +
                "ac.*\n" +
                "FROM\n" +
                "\tcustomer_lawyers cl \n" +
                "\tleft JOIN  agreement_customer ac on cl.customerId = ac.id " +
                "WHERE\n" +
                "\t1 = 1 \n" +
                "\tAND ac.isDel = 0 \n" +
                "\tAND ac.parentId = 0 \n" ;
        if (Objects.nonNull(vo.getUserId())){
            sql+= "\tAND cl.lawyerId = "+vo.getUserId()+" \n";
        }
        if (Objects.nonNull(vo.getInService())){
            sql += "\tAND ac.inService = "+vo.getInService()+" \n";
        }
        if (StrUtil.isNotBlank(vo.getCreditCode())){
            sql += "\tAND ac.creditCode LIKE '%"+vo.getCreditCode()+"%' \n";
        }
        if (StrUtil.isNotBlank(vo.getCustomerName())){
            sql += "\tAND ac.customerName LIKE '%"+vo.getCustomerName()+"%' \n";
        }
        if (StrUtil.isNotBlank(vo.getCustomerCode())){
            sql += "\tAND ac.customerCode LIKE '%"+vo.getCustomerCode()+"%' \n";
        }
        if (Objects.nonNull(vo.getFeeStart())){
            sql += "\tAND ac.serviceFee >= "+vo.getFeeStart()+" \n";
        }
        if (Objects.nonNull(vo.getFeeEnd())){
            sql += "\tAND ac.serviceFee <= "+vo.getFeeEnd()+" \n";
        }
        sql+="ORDER BY ac.createTime DESC";
        return sql;
    }


    public String getList(Integer id) {
        String sql = "SELECT\n" +
                "\tac.* \n" +
                "FROM\n" +
                "\tagreement_customer ac\n" +
                "\tLEFT JOIN customer_lawyers cl ON ac.id = cl.customerId \n" +
                "WHERE\n" +
                "\t1 = 1 \n" +
                "\tAND cl.lawyerId = "+id+" \n"+
                "\tAND ac.isDel = 0 \n" +
                "\tAND ac.inService = 1 \n" ;
        return sql;
    }
}
