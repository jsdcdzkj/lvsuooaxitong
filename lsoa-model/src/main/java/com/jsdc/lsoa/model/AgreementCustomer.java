package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@TableName("agreement_customer")
@Table(name = "agreement_customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementCustomer {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ApiModelProperty("客户编号")
    private String customerCode;


    @ApiModelProperty("客户名称")
    private String customerName;


    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("信用代码")
    private String creditCode;


    @ApiModelProperty("服务费")
    private BigDecimal serviceFee;


    @ApiModelProperty("是否在服务期（0：不在，1：在）")
    private Integer inService;

    @ApiModelProperty("协议起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date starTime;

    @ApiModelProperty("协议终止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("删除标记（0正常 1删除）")
    private Integer isDel;

    @ApiModelProperty("登记人")
    private Integer createUser;

    @ApiModelProperty("登记时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("法人")
    private String legalPerson;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("文件id")
    private Integer fileId;

    @ApiModelProperty("开票状态（1未开票 2待审批 3已开票 4已退回）")
    private String invoice;

    @ApiModelProperty("退回备注")
    private String returnRemarks;
}
