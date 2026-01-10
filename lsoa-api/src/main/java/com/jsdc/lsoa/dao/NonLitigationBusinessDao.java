package com.jsdc.lsoa.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.NonLitigationBusiness;
import com.jsdc.lsoa.vo.NonLitigationBusinessVO;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class NonLitigationBusinessDao extends BaseDao<NonLitigationBusiness> {


    public String getPage(Page<NonLitigationBusiness> page, NonLitigationBusinessVO vo, boolean hasSpecialPermission) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, businessType, checkDesc, checkResult, checkTime, checkUser, code, companyCode, companyName," +
                " createTime, createUser, isDel, lawyerId, legalPerson, phone, status, updateTime, updateUser from non_litigation_business where isDel = 0");
        if (hasSpecialPermission) {
            sql.append(" and (status > 0 or createUser = ").append(vo.getLoginUser()).append(")");
        }else {
            sql.append(" and (createUser = ").append(vo.getLoginUser()).append(" or find_in_set('").append(vo.getLoginUser()).append("', lawyerId))");
        }
        if (StrUtil.isNotBlank(vo.getCode())) {
            sql.append(" and code = '").append(vo.getCode()).append("'");
        }
        if (StrUtil.isNotBlank(vo.getLegalPerson())) {
            sql.append(" and legalPerson like '%").append(vo.getLegalPerson()).append("%'");
        }
        if (StrUtil.isNotBlank(vo.getLawyerId())) {
            sql.append(" and find_in_set('").append(vo.getLawyerId()).append("', lawyerId)");
        }
        if (Objects.nonNull(vo.getBusinessType())) {
            sql.append(" and businessType = ").append(vo.getBusinessType());
        }
        if (StrUtil.isNotBlank(vo.getBeginDate())) {
            sql.append(" and createTime >= '").append(vo.getBeginDate()).append(" 00:00:00'");
        }
        if (StrUtil.isNotBlank(vo.getEndDate())) {
            sql.append(" and createTime <= '").append(vo.getEndDate()).append(" 23:59:59'");
        }
        if (Objects.nonNull(vo.getStatus())) {
            sql.append(" and status = ").append(vo.getStatus());
        }
        sql.append(" order by id desc");
        System.out.println(sql.toString());
        return sql.toString();
    }

}
