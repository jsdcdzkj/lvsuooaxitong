package com.jsdc.lsoa.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.ProcessDesign;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.vo.CaseInfoVo;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ProcessDesignDao extends BaseDao<ProcessDesign>  {



    public String  toInvoice(String id, SysUser user) {
        StringBuilder sql = new StringBuilder("\n" +
                "SELECT * FROM process_design WHERE 1=1");
        if (StrUtil.isNotBlank(id)) {
            sql.append(" AND id = ").append(id);
            if (user!=null) {
                sql.append("\n AND ");
                sql.append(" (\n" + "\t\tFIND_IN_SET( '").append(user.getId()).append("', sponsorValue ) \n");
                if(!StringUtils.isEmpty(user)){
                    for (SysRole sysRole : user.getRoleList()) {
                        sql.append("\t\t OR FIND_IN_SET( '").append(sysRole.getId()).append("', sponsorValue ) \n");
                    }
                }
                sql.append(")\n");
            }
        }
        return sql.toString();
    }
}
