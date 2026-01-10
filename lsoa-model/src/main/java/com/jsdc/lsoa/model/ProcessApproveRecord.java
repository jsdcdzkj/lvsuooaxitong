package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("PROCESS_APPROVE_RECORD")
@Table(name = "PROCESS_APPROVE_RECORD")
@Data
@Accessors
public class ProcessApproveRecord {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "流程设计表ID")
    private Integer processId;

    @ApiModelProperty(value = "流程类型 1：开函，2：开票（金额小于配置金额），3：开票（金额大于配置金额）,4 顾问客户开票")
    private Integer processType;

    @ApiModelProperty(value = "流程节点ID")
    private Integer processNodeId;

    @ApiModelProperty(value = "节点类型 1：审批，2：抄送")
    private Integer nodeType;

    @ApiModelProperty(value = "案件ID")
    private Integer caseId;

    @ApiModelProperty(value = "开函记录ID")
    private Integer summonsInfoId;

    @ApiModelProperty(value = "开票记录ID")
    private Integer invoiceInfoId;

    @ApiModelProperty(value = "类型 1：人员，2：角色")
    private Integer approverType;

    @ApiModelProperty(value = "人员ID或角色ID，多个采用英文逗号分隔")
    private String approverValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "审批状态 0：未审批，1：审批通过，2：退回")
    private Integer status;

    @ApiModelProperty(value = "审批意见")
    private String approveDesc;

    @ApiModelProperty(value = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveTime;

    @ApiModelProperty(value = "审批人")
    private Integer approveUser;

    @ApiModelProperty(value = "审批人姓名")
    private String approveUserName;

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
