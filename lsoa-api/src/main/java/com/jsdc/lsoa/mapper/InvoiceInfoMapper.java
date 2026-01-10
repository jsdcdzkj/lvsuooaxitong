package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface InvoiceInfoMapper extends BaseMapper<InvoiceInfo> {

    @SelectProvider(type = InvoiceInfoDao.class, method = "getEntityList")
    List<InvoiceInfoVo> getEntityList( CaseInfoVo bean);


    @SelectProvider(type = InvoiceInfoDao.class, method = "selectInvoicePage")
    Page<CaseInfoVo> selectInvoicePage(Page page, CaseInfoVo vo);

    @SelectProvider(type = InvoiceInfoDao.class, method = "selectInvoiceList")
    List<CaseInfoVo> selectInvoiceList(CaseInfoVo vo);
}
