package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.CustomerLawyersDao;
import com.jsdc.lsoa.model.CustomerLawyers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface CustomerLawyersMapper extends BaseMapper<CustomerLawyers> {

    @SelectProvider(type = CustomerLawyersDao.class, method = "getEntityList")
    Page<CustomerLawyers> getEntityList(Page page, CustomerLawyers bean);
}
