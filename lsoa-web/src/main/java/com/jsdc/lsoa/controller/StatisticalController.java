package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.StatisticalService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.StatisticalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wr
 * @Description: 统计分析
 */
@RestController
@RequestMapping("/api/statistical")
@Api(tags = "统计分析")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;
    /**
     * 案件类型数量排名
     */
    @PostMapping("/CaseTypeRanking")
    @ApiOperation("案件类型数量排名")
    public ResultInfo CaseTypeRanking(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.CaseTypeRanking(vo));
    }

    /**
     * 案件类型金额排名
     */
    @PostMapping("/CaseTypeMoneyRanking")
    @ApiOperation("案件类型金额排名")
    public ResultInfo CaseTypeMoneyRanking(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.CaseTypeMoneyRanking(vo));
    }

    @PostMapping("/getStatisticalVoWithZero")
    @ApiOperation("律所案件柱状图统计")
    public ResultInfo getStatisticalVoWithZero(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.getStatisticalVoWithZero(vo));
    }

    @PostMapping("/getStatisticalVoWithZeroMoney")
    @ApiOperation("律所案件柱状图统计金额版")
    public ResultInfo getStatisticalVoWithZeroMoney(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.getStatisticalVoWithZeroMoney(vo));
    }


    @PostMapping("/lawFirmCaseStatisticsByType")
    @ApiOperation("律所各类型案件统计")
    public ResultInfo lawFirmCaseStatisticsByType(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.lawFirmCaseStatisticsByType(vo));
    }

    @PostMapping("/lawFirmCaseStatisticsByTypeMoney")
    @ApiOperation("律所各类型案件统计金额版")
    public ResultInfo lawFirmCaseStatisticsByTypeMoney(@RequestBody StatisticalVo vo){
        return ResultInfo.success(statisticalService.lawFirmCaseStatisticsByTypeMoney(vo));
    }


    /**
     * 导出表格
     */
    @RequestMapping("/export")
    @ApiOperation("导出表格")
    public void export(StatisticalVo vo,HttpServletResponse response) throws IOException {
        statisticalService.export(vo,"count",response);
    }

    /**
     * 导出表格金额版
     */
    @RequestMapping("/exportMoney")
    @ApiOperation("导出表格金额版")
    public void exportMoney(StatisticalVo vo ,HttpServletResponse response) throws IOException {
        statisticalService.export(vo,"money",response);
    }

    /**
     * 获取普通律师列表
     */
    @PostMapping("/getNormalUserList")
    @ApiOperation("获取普通律师列表")
    public ResultInfo getNormalUserList(){
        return ResultInfo.success(statisticalService.getNormalUserList());
    }
}
