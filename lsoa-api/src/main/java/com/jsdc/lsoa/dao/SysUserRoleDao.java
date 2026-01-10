package com.jsdc.lsoa.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.SysUserRole;
import org.springframework.stereotype.Repository;

/**
 * @Author ：苹果
 * @Description：
 * @Date ：2024/12/19 14:30
 * @Modified By：
 */
@Repository
public class SysUserRoleDao extends BaseDao<SysUserRole> {


    public String getEntityList(String  id) {
        String sql = " \t\n" +
                "\t\n" +
                "SELECT\n" +
                "\tb.* \n" +
                "FROM\n" +
                "\tsys_user_role a\n" +
                "\tLEFT JOIN sys_role b ON a.roleId = b.id \n" +
                "WHERE\n" +
                "\ta.isDel = 0 \n" +
                "\tAND a.userId = "+id+"\n" +
                "\t";

        return sql;
    }

    public String getNormalUserList() {
        String sql = "SELECT\n" +
                "\tsu.id,su.realName\n" +
                "FROM\n" +
                "\tsys_user su\n" +
                "LEFT JOIN sys_user_role sur on sur.userId = su.id  AND sur.isDel = 0\n" +
                "LEFT JOIN sys_role sr on sr.id = sur.roleId AND sr.isDel = 0\n" +
                "WHERE sr.roleCode = '"+ CommonConstants.NORMAL_SOLICITOR +"'  ";
        return sql;
    }

}
