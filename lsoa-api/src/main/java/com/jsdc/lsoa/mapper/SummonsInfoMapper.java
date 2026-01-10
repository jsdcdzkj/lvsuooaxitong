package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.dao.SummonsInfoDao;
import com.jsdc.lsoa.model.SummonsInfo;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.SummonsInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SummonsInfoMapper extends BaseMapper<SummonsInfo> {

    @SelectProvider(type = SummonsInfoDao.class, method = "getEntityList")
    List<SummonsInfoVo> getEntityList(CaseInfoVo bean);

    @SelectProvider(type = SummonsInfoDao.class, method = "selectSummonsPage")
    Page<CaseInfoVo>selectSummonsPage(Page page, CaseInfoVo vo);

    @SelectProvider(type = SummonsInfoDao.class, method = "selectAPPSummonsPage")
    Page<CaseInfoVo> selectAPPSummonsPage(Page page, CaseInfoVo vo);

    @SelectProvider(type = SummonsInfoDao.class, method = "selectSummonsList")
    List<CaseInfoVo>selectSummonsList(CaseInfoVo vo);
}
