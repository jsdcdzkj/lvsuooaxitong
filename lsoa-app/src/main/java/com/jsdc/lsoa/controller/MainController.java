package com.jsdc.lsoa.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.service.MainService;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.vo.LoginVO;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/app")
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

    @PostMapping("/user")
    @ApiOperation("获取当前登录用户")
    public ResultInfo<SysUser> userInfo() {
        return ResultInfo.success(userService.getUser());
    }

}
