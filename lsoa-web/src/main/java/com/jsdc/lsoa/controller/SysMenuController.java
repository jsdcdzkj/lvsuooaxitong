package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysMenuService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author ：苹果
 * @Description：
 * @Date ：2024/12/19 16:10
 * @Modified By：
 */
@RequestMapping("/api/sysMenu")
@Api(tags = "菜单管理")
@RestController
public class SysMenuController {

    @Autowired
    private SysMenuService service;

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultInfo getPage(@RequestBody SysMenuVo vo){
        return  ResultInfo.success(service.getPage(vo));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增编辑")
    @LogInfo(type = "新增/编辑", mode = "菜单管理")
    public ResultInfo saveOrUpdate(@RequestBody SysMenuVo vo){
        service.saveOrUpdateMenu(vo);
        return  ResultInfo.success();
    }

    @PostMapping("/del")
    @ApiOperation("删除")
    @LogInfo(type = "删除", mode = "菜单管理")
    public ResultInfo del(@RequestBody SysMenuVo vo){
        return  service.del(vo);
    }

    @ApiOperation("根据ID查询")
    @PostMapping("/getSysMenu")
    public ResultInfo getSysMenu(@RequestBody SysMenuVo vo){
        return  ResultInfo.success(service.getSysMenu(vo));
    }


    @ApiOperation("菜单树形结构")
    @PostMapping("/getSysMenuTree")
    public ResultInfo getSysMenuTree(@RequestBody SysMenuVo vo){
        return  ResultInfo.success(service.getSysMenuTree(vo));
    }

    @ApiOperation("列表查询")
    @PostMapping("/getSysMenuList")
    public ResultInfo getSysMenuList(@RequestBody SysMenuVo vo){
        return  ResultInfo.success(service.getSysMenuList(vo));
    }



}
