package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.dao.SysUserRoleDao;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.model.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @SelectProvider(type = SysUserRoleDao.class, method = "getEntityList")
    List<SysRole> getEntityList( String id);

    @SelectProvider(type = SysUserRoleDao.class, method = "getNormalUserList")
    List<SysUser> getNormalUserList();
}
