package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@TableName("SYS_MENU")
@Table(name = "SYS_MENU")
@Data
@Accessors
public class SysMenu {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "父级菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "路由名称")
    private String routeName;

    @ApiModelProperty(value = "路由地址")
    private String routeUrl;

    @ApiModelProperty(value = "页面路径")
    private String vueUrl;

    @ApiModelProperty(value = "重定向类型")
    private String redirectType;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否展示（0：不显示 1：显示）")
    private String isShow;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "菜单类型（1:菜单 2:按钮）")
    private String menuType;

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

    @TableField(exist = false)
    @Transient
    private List<SysMenu> MenuChild;
}
