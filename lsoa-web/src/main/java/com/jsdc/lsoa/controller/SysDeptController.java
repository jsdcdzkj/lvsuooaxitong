package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysDeptService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDeptVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author ：苹果
 * @Description：
 * @Date ：2024/12/19 16:10
 * @Modified By：
 */
@RequestMapping("/api/sysDept")
@Api(tags = "部门管理")
@RestController
public class SysDeptController {

    @Autowired
    private SysDeptService service;


    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultInfo getPage(@RequestBody SysDeptVo vo){
        return  ResultInfo.success(service.getPage(vo));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增编辑")
    @LogInfo(type = "新增/编辑", mode = "部门管理")
    public ResultInfo saveOrUpdate(@RequestBody SysDeptVo vo){

        return  service.saveOrUpdateDept(vo);
    }

    @PostMapping("/del")
    @ApiOperation("删除")
    @LogInfo(type = "删除", mode = "部门管理")
    public ResultInfo del(@RequestBody SysDeptVo vo){
        return  service.del(vo);
    }


    @ApiOperation("根据ID查询")
    @PostMapping("/getSysDept")
    public ResultInfo getSysDept(@RequestBody SysDeptVo vo){
        return  ResultInfo.success(service.getSysDept(vo));
    }


    @ApiOperation("列表查询")
    @PostMapping("/getSysDeptList")
    public ResultInfo getSysDeptList(@RequestBody SysDeptVo vo){
        return  ResultInfo.success(service.getSysDeptList(vo));
    }

}
