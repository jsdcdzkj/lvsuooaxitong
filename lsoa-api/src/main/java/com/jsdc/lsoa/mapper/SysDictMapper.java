package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.SysDictDao;
import com.jsdc.lsoa.model.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    @SelectProvider(type = SysDictDao.class, method = "getEntityList")
    Page<SysDict> getEntityList(Page page, SysDict bean);
}
