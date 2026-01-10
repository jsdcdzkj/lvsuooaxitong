package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("SYS_USER_POST")
@Table(name = "SYS_USER_POST")
@Data
@Accessors
public class SysUserPost {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "岗位ID")
    private Integer postId;

    @ApiModelProperty(value = "岗位性质（1：主岗，2：副岗）")
    private Integer type;

    @ApiModelProperty(value = "删除标记（0：否，1：是）")
    private Integer isDel;

    @ApiModelProperty(value = "创建者")
    private Integer createUser;

    @ApiModelProperty(value = "修改者")
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
