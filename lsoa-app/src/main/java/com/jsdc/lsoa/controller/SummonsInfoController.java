package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.SummonsInfoService;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SummonsInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  todo  
 *  controller控制器
 */
@RestController
@RequestMapping("/app/summonsInfo")
@Api(tags = "开函")
public class SummonsInfoController {

    @Autowired
    private SummonsInfoService summonsInfoService;

    /**
     * 分页查询 todo
     *
     * @return
     */
    @PostMapping("/getPageList")
    @ApiOperation("查询")
    public ResultInfo getPageList(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(summonsInfoService.selectAPPSummonsPage(bean));
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("开函")
    public ResultInfo saveOrUpdateSummonsInfo(@RequestBody SummonsInfoVo bean) {
        return summonsInfoService.saveOrUpdateSummonsInfo(bean);
    }

    /**
     * 获取实体类
     * @param bean
     * @return
     */
    @PostMapping("/getEntity")
    @ApiOperation("详情")
    public ResultInfo getEntity(@RequestBody SummonsInfoVo bean) {
        return summonsInfoService.getEntityById(bean.getId());
    }


    @PostMapping("/approveInvoiceInfo")
    @ApiOperation("审批")
    public ResultInfo  approveInvoiceInfo(@RequestBody SummonsInfoVo bean){
        return summonsInfoService.approveInvoiceInfo(bean);
    }


    /**
     * 开函详情查询 todo
     *
     * @return
     */
    @PostMapping("/getList")
    @ApiOperation("开函详情查询")
    public ResultInfo getList(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(summonsInfoService.getList(bean));
    }
}
