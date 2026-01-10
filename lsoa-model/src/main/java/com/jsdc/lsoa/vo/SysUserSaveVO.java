package com.jsdc.lsoa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysUserSaveVO implements Serializable {

    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "姓名全拼")
    private String nameSpelling;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String cardId;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "账号状态（0：正常，1：禁用）")
    private Integer status;

    @ApiModelProperty(value = "用户性质（0：内部，1：外部）")
    private Integer type;

    @ApiModelProperty(value = "用户角色ID列表")
    private List<Integer> roleList;

//    @ApiModelProperty(value = "主岗岗位ID")
//    private Integer mainPost;
//
//    @ApiModelProperty(value = "副岗岗位ID列表")
//    private List<Integer> deputyPost;

}
