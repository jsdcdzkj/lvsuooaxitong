package com.jsdc.lsoa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPageVO extends PageVO{

    @ApiModelProperty(value = "UUID")
    private String uuid;

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "账号状态（0：正常，1：禁用）")
    private Integer status;

    @ApiModelProperty(value = "用户性质（0：内部，1：外部）")
    private Integer type;
}
