package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.ClientInfoDao;
import com.jsdc.lsoa.model.ClientInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface ClientInfoMapper extends BaseMapper<ClientInfo> {

    @SelectProvider(type = ClientInfoDao.class, method = "getEntityList")
    Page<ClientInfo> getEntityList(Page page, ClientInfo bean);
}
