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
@TableName("PROCESS_NODE")
@Table(name = "PROCESS_NODE")
@Data
@Accessors
public class ProcessNode {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "流程ID")
    private Integer processId;

    @ApiModelProperty(value = "节点类型 1：审批，2：抄送")
    private Integer nodeType;

    @ApiModelProperty(value = "类型 1：人员，2：角色")
    private Integer approverType;

    @ApiModelProperty(value = "人员ID或角色ID，多个采用英文逗号分隔")
    private String approverValue;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "人员或角色名称")
    private String approverName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

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
