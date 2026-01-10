package com.jsdc.lsoa.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.vo.SysUserPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class SysUserDao extends BaseDao<SysUser>  {


    public String getPage(Page<SysUser> page, SysUserPageVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select su.id, su.avatar, su.cardId, su.createTime, su.createUser, su.deptId, su.isDel, su.nameSpelling, su.password, su.phone, su.realName, su.status, su.updateTime, su.updateUser, su.workId");
        sql.append(" from sys_user su");
        sql.append(" where su.isDel = 0");
        if (Objects.nonNull(vo.getStatus())) {
            sql.append(" and su.status = ").append(vo.getStatus());
        }
        if (Objects.nonNull(vo.getDeptId())) {
            sql.append(" and su.deptId = ").append(vo.getDeptId());
        }
        if (Objects.nonNull(vo.getRoleId())) {
            sql.append(" and EXISTS (select 1 from sys_user_role sur where sur.userId = su.id and sur.roleId = ").append(vo.getRoleId()).append(" and sur.isDel = 0)");
        }
        if (StringUtils.isNotBlank(vo.getWorkId())) {
            sql.append(" and su.workId like '%").append(vo.getWorkId()).append("%'");
        }
        if (StringUtils.isNotBlank(vo.getPhone())) {
            sql.append(" and su.phone like '%").append(vo.getPhone()).append("%'");
        }
        if (StringUtils.isNotBlank(vo.getRealName())) {
            sql.append(" and su.realName like '%").append(vo.getRealName()).append("%'");
        }
        sql.append(" order by id");
        return sql.toString();
    }

}
