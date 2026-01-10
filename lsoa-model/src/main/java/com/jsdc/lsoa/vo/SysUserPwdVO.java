package com.jsdc.lsoa.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysUserPwdVO {

    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;

}
