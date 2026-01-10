package com.jsdc.lsoa.controller;


import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysUserPwdVO;
import com.jsdc.lsoa.vo.SysUserSaveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/app/sysuser")
@Api(tags = "用户管理")
public class SysUserController {


    @Autowired
    private SysUserService userService;


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


    @LogInfo(type = "修改密码", mode = "用户管理")
    @ApiOperation("修改密码")
    @PostMapping("/password")
    public ResultInfo<Void> changePassword(@RequestBody SysUserPwdVO vo) {
        userService.changePassword(vo);
        return ResultInfo.success();
    }

}
