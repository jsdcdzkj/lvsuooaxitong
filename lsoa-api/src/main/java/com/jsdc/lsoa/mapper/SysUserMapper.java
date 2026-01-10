package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.SysUserDao;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.vo.SysUserPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    @SelectProvider(type = SysUserDao.class, method = "getPage")
    Page<SysUser> getPage(Page<SysUser> page, SysUserPageVO vo);
}
