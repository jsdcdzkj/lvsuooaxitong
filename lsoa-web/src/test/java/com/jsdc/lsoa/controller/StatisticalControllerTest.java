package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.StatisticalService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.StatisticalVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class StatisticalControllerTest {

    @Autowired
    private StatisticalController statisticalController;
    @Autowired
    private StatisticalService service;

    @Test
    void caseTypeRanking() {
        StatisticalVo statisticalVo = new StatisticalVo();
        statisticalVo.setFlag("day");
        statisticalVo.setStartTime("2025-01");
        statisticalVo.setEndTime("2025-04");
//        statisticalVo.setFlag("month");
//        statisticalVo.setTime("2025-02");
//        statisticalVo.setFlag("year");
//        statisticalVo.setTime("2025");
//        statisticalVo.setCaseType("1");
//        ResultInfo resultInfo = statisticalController.CaseTypeRanking(statisticalVo);
        ResultInfo resultInfo = statisticalController.CaseTypeMoneyRanking(statisticalVo);
        System.out.println(resultInfo);
    }

    @Test
    void getStatisticalVoWithZero() {
        StatisticalVo statisticalVo = new StatisticalVo();
        statisticalVo.setFlag("day");
        statisticalVo.setStartTime("2025-01");
        statisticalVo.setEndTime("2025-04");
//        statisticalVo.setFlag("month");
//        statisticalVo.setTime("2025-02");
//        statisticalVo.setFlag("year");
//        statisticalVo.setTime("2025");
        ResultInfo statisticalVoWithZero = statisticalController.getStatisticalVoWithZero(statisticalVo);
//                ResultInfo a = statisticalController.getStatisticalVoWithZeroMoney(statisticalVo);
        System.out.println();
    }
    @Test
    void a() {
        StatisticalVo statisticalVo = new StatisticalVo();
//        statisticalVo.setFlag("day");
//        statisticalVo.setStartTime("2025-01-10 00:00:00");
//        statisticalVo.setEndTime("2025-02-20 23:59:59");
        statisticalVo.setFlag("month");
        statisticalVo.setTime("2025-02");
//        statisticalVo.setFlag("year");
//        statisticalVo.setTime("2025");
//        statisticalVo.setCaseType("2");
        ResultInfo a = statisticalController.lawFirmCaseStatisticsByType(statisticalVo);
//        ResultInfo a = statisticalController.lawFirmCaseStatisticsByTypeMoney(statisticalVo);

        System.out.println();
    }
}