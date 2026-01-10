package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.SysLogDao;
import com.jsdc.lsoa.model.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    @SelectProvider(type = SysLogDao.class, method = "getEntityList")
    Page<SysLog> getEntityList(Page page, SysLog bean);
}
