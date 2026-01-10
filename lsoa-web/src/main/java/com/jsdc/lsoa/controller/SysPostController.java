package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysPostService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysPostVo;
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
@RequestMapping("/api/sysPost")
@Api(tags = "岗位管理")
@RestController
public class SysPostController {

    @Autowired
    private SysPostService service;

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultInfo getPage(@RequestBody SysPostVo vo){
        return  ResultInfo.success(service.getPage(vo));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增编辑")
    @LogInfo(type = "新增/编辑", mode = "岗位管理")
    public ResultInfo saveOrUpdate(@RequestBody SysPostVo vo){
        return service.saveOrUpdatePost(vo);
    }

    @PostMapping("/del")
    @ApiOperation("删除")
    @LogInfo(type = "删除", mode = "岗位管理")
    public ResultInfo del(@RequestBody SysPostVo vo){
        return  service.del(vo);
    }

    @ApiOperation("根据ID查询")
    @PostMapping("/getSysPost")
    public ResultInfo getSysPost(@RequestBody SysPostVo vo){
        return  ResultInfo.success(service.getSysPost(vo));
    }


    @ApiOperation("列表查询")
    @PostMapping("/getSysPostList")
    public ResultInfo getSysPostList(@RequestBody SysPostVo vo){
        return  ResultInfo.success(service.getSysPostList(vo));
    }

}
