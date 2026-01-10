package com.jsdc.lsoa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/sysuser")
@Api(tags = "用户管理")
public class SysUserController {


    @Autowired
    private SysUserService userService;


    @PostMapping("/page")
    @ApiOperation("分页")
    public ResultInfo<Page<SysUser>> page(@RequestBody SysUserPageVO vo) {
        Page<SysUser> page = userService.page(vo);
        return ResultInfo.success(page);
    }


    @PostMapping("/list")
    @ApiOperation("列表（不分页）")
    public ResultInfo<List<SysUser>> list() {
        List<SysUser> list = userService.list();
        return ResultInfo.success(list);
    }

    @LogInfo(type = "新增/编辑", mode = "用户管理")
    @ApiOperation("新增或修改")
    @PostMapping("/save")
    public ResultInfo<Void> save(@RequestBody SysUserSaveVO vo) {
        userService.save(vo);
        return ResultInfo.success();
    }

    @ApiOperation("上传头像")
    @PostMapping("/avatar")
    public ResultInfo<String> avatar(MultipartFile file) {
        String fileUrl = userService.uploadAvatar(file);
        return ResultInfo.success(fileUrl);
    }

    @LogInfo(type = "删除", mode = "用户管理")
    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResultInfo<Void> delete(@RequestBody SysUser entity) {
        userService.delete(entity);
        return ResultInfo.success();
    }

    @LogInfo(type = "重置密码", mode = "用户管理")
    @ApiOperation("重置密码")
    @PostMapping("/reset/password")
    public ResultInfo<Void> resetPassword(@RequestBody SysUserPwdVO vo) {
        userService.resetPassword(vo);
        return ResultInfo.success();
    }

}
