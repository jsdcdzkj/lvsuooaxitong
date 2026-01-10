package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.CaseInfoService;
import com.jsdc.lsoa.service.StatisticalService;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wr
 * @Description: 案件
 */
@RestController
@RequestMapping("/app/caseInfo")
@Api(tags = "案件管理")
public class CaseInfoController {

    @Autowired
    private CaseInfoService caseInfoService;
    @Autowired
    private StatisticalService statisticalService;

    /**
     * 分页查询
     */
    @PostMapping("/getPageList")
    @ApiOperation("分页查询")
    public ResultInfo getPageList(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(caseInfoService.getPageList(bean));
    }

    /**
     * 添加案件
     */
    @PostMapping("/saveCaseInfo")
    @ApiOperation("添加案件")
    public ResultInfo saveCaseInfo(@RequestBody CaseInfoVo bean) {
        return caseInfoService.saveCaseInfo(bean);
    }
    /**
     * 编辑案件
     */
    @PostMapping("/updateCaseInfo")
    @ApiOperation("编辑案件")
    public ResultInfo updateCaseInfo(@RequestBody CaseInfoVo bean) {
        caseInfoService.updateCaseInfo(bean);
        return ResultInfo.success();
    }
    /**
     * 编辑当事人
     */
    @PostMapping("/updateClientInfo")
    @ApiOperation("编辑当事人")
    public ResultInfo updateClientInfo(@RequestBody CaseInfoVo bean) {
        caseInfoService.updateClientInfo(bean);
        return ResultInfo.success();
    }
    /**
     * 编辑案件文件
     */
    @PostMapping("/updateCaseFile")
    @ApiOperation("编辑案件文件")
    public ResultInfo updateCaseFile(@RequestBody CaseInfoVo bean) {
        caseInfoService.updateCaseFile(bean);
        return ResultInfo.success();
    }

    /**
     * 编辑案件
     */
    @PostMapping("editCase")
    @ApiOperation("编辑案件")
    public ResultInfo editCase(@RequestBody CaseInfoVo bean) {
        return caseInfoService.editCase(bean);
    }

    /**
     * 详情
     * @param bean
     * @return
     */
    @PostMapping("/getEntity")
    @ApiOperation("详情")
    public ResultInfo getEntity(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(caseInfoService.getEntity(bean.getId()));
    }

    /**
     * 删除
     */
    @ApiOperation("删除案件")
    @PostMapping("/del")
    public ResultInfo del(@RequestBody CaseInfoVo bean) {
        return caseInfoService.del(bean.getId());
    }

    /**
     * 获取普通律师列表
     */
    @PostMapping("/getNormalUserList")
    @ApiOperation("获取普通律师列表")
    public ResultInfo getNormalUserList(){
        return ResultInfo.success(statisticalService.getNormalUserList());
    }



    /**
     * 待办
     */
    @PostMapping("/toDo")
    @ApiOperation("待办")
    public ResultInfo toDo(@RequestBody CaseInfoVo bean) {
        return caseInfoService.toDo(bean);
    }




    /**
     * 已办
     */
    @PostMapping("/finish")
    @ApiOperation("已办")
    public ResultInfo finish(@RequestBody CaseInfoVo bean) {
        return caseInfoService.finish(bean);
    }
    /**
     * 归档
     */
    @PostMapping("/archive")
    @ApiOperation("归档")
    public ResultInfo archive(@RequestBody CaseInfoVo bean) {
        return caseInfoService.archive(bean.getId());
    }
}
