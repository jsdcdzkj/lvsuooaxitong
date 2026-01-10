package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.dao.ProcessDesignDao;
import com.jsdc.lsoa.model.ProcessDesign;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.vo.CaseInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface ProcessDesignMapper extends BaseMapper<ProcessDesign> {



    @SelectProvider(type = ProcessDesignDao.class, method = "toInvoice")
    ProcessDesign toInvoice(String id, SysUser user);
}
