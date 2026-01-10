package com.jsdc.lsoa.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.mapper.SysUserMapper;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.service.MainService;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.utils.MD5Utils;
import com.jsdc.lsoa.utils.PasswordUtil;
import com.jsdc.lsoa.vo.LoginVO;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysUserPwdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@Api(tags = "登录管理")
@RestController
public class MainController {


    @Autowired
    private MainService mainService;

    @Autowired
    private SysUserService userService;

    @LogInfo(type = "登录", mode = "登录")
    @ApiOperation("登录")
    @PostMapping("/login")
    public ResultInfo<SaTokenInfo> login(@RequestBody LoginVO vo) {
        SaTokenInfo token = mainService.webLogin(vo);
        return ResultInfo.success(token);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public ResultInfo<Void> logout() {
        mainService.webLogout();
        return ResultInfo.success();
    }


    @GetMapping("/user")
    @ApiOperation("获取当前登录用户")
    public ResultInfo<SysUser> userInfo() {
        return ResultInfo.success(userService.getUser());
    }


    @PostMapping("/validate")
    @ApiOperation("校验密码强度")
    public ResultInfo<Void> validatePassword(@RequestBody String password) {
        PasswordUtil.validate(password);
        return ResultInfo.success();
    }

    @LogInfo(type = "修改密码", mode = "登录管理")
    @ApiOperation("修改密码")
    @PostMapping("/change/password")
    public ResultInfo<Void> changePassword(@RequestBody SysUserPwdVO vo) {
        userService.changePassword(vo);
        return ResultInfo.success();
    }

}
