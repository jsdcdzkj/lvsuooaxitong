package com.jsdc.lsoa.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("PROCESS_DESIGN")
@Table(name = "PROCESS_DESIGN")
@Data
@Accessors
public class ProcessDesign {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "流程类型 1：开函，2：开票（金额小于配置金额），3：开票（金额大于配置金额）")
    private Integer processType;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "流程说明")
    private String description;

    @ApiModelProperty(value = "是否启用 Y：启用，N：停用")
    private String isEnable;

    @ApiModelProperty(value = "发起人类型 1：人员，2：角色")
    private Integer sponsorType;

    @ApiModelProperty(value = "发起人或角色ID")
    private String sponsorValue;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "发起人或角色名称")
    private String sponsorName;

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

}
