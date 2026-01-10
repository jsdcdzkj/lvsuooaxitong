package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("SYS_LOG")
@Table(name = "SYS_LOG")
@Data
@Accessors
public class SysLog {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    @ApiModelProperty(value = "模块名称")
    private String modeName;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "操作地址")
    private String requestIp;

    @ApiModelProperty(value = "接口地址")
    private String api;

    @ApiModelProperty(value = "请求参数")
    private String requestBody;

    @ApiModelProperty(value = "响应参数")
    private String responseBody;

    @ApiModelProperty(value = "状态（0：成功，1：失败）")
    private Integer status;

    @ApiModelProperty(value = "操作人")
    private Integer operator;

    @ApiModelProperty(value = "操作人")
    private String operatorName;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

}
