package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.CaseInfoDao;
import com.jsdc.lsoa.dao.InvoiceInfoDao;
import com.jsdc.lsoa.model.CaseInfo;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.StatisticalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface CaseInfoMapper extends BaseMapper<CaseInfo> {

    @SelectProvider(type = CaseInfoDao.class, method = "getEntityList")
    Page<CaseInfo> getEntityList(Page page, CaseInfo bean);
    @SelectProvider(type = CaseInfoDao.class, method = "selectPageWhere")
    Page<CaseInfoVo> selectPageWhere(Page page, CaseInfoVo vo);
    @SelectProvider(type = CaseInfoDao.class, method = "CaseTypeRanking")
    List<StatisticalVo> CaseTypeRanking(StatisticalVo vo,String type);

    // 按时间段统计（每天）
    @SelectProvider(type = CaseInfoDao.class, method = "statByDay")
    List<StatisticalVo> statByDay(StatisticalVo vo,String type);

    // 按月统计（每天）
    @SelectProvider(type = CaseInfoDao.class, method = "statByDayOfMonth")
    List<StatisticalVo> statByDayOfMonth(StatisticalVo vo,String type);

    // 按年统计（每月）
    @SelectProvider(type = CaseInfoDao.class, method = "statByMonthOfYear")
    List<StatisticalVo> statByMonthOfYear(StatisticalVo vo,String type);


    @SelectProvider(type = CaseInfoDao.class, method = "getCaseStatistics")
    List<Map<String,String>> getCaseStatistics(StatisticalVo vo,List<SysDict> caseTypes,String type);


    @SelectProvider(type = CaseInfoDao.class, method = "toDo")
    Page<CaseInfoVo> toDo(Page page, CaseInfoVo vo);


    @SelectProvider(type = CaseInfoDao.class, method = "finish")
    Page<CaseInfoVo> finish(Page page, CaseInfoVo vo);

}
