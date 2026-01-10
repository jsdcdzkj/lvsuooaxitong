package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@TableName("SYS_USER")
@Table(name = "SYS_USER")
@Data
public class SysUser implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String cardId;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "账号状态（0：正常，1：禁用）")
    private Integer status;

    @ApiModelProperty(value = "删除标记（0：否，1：是）")
    private Integer isDel;

    @ApiModelProperty(value = "创建者")
    private Integer createUser;

    @ApiModelProperty(value = "修改者")
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "用户角色列表")
    private List<SysRole> roleList;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "主岗")
    private SysPost mainPost;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "副岗")
    private List<SysPost> deputyPost;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "部门")
    private SysDept dept;

}
