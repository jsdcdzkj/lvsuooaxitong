package com.jsdc.lsoa.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@TableName("invoice_info")
@Table(name = "invoice_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceInfo extends Model<InvoiceInfo> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("案件id")
    private String caseId;

    @ApiModelProperty("发票内容")
    private String content;

    @ApiModelProperty("开票金额")
    private String invoiceMoney;

    @ApiModelProperty("合同金额")
    private String contractMoney;

    @ApiModelProperty("发票类型")
    private String invoiceType;

    @ApiModelProperty("联系人")
    private String contactPerson;

    @ApiModelProperty("联系人电话")
    private String contactPhone;

    @ApiModelProperty("邮箱")
    private String mail;

    @ApiModelProperty("收件地址")
    private String address;

    @ApiModelProperty("收件人")
    private String addressee;

    @ApiModelProperty("收件人电话")
    private String addresseePhone;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("开户行")
    private String bankDeposit;

    @ApiModelProperty("注册电话")
    private String registerTel;

    @ApiModelProperty("注册地址")
    private String registeredAdd;

    @ApiModelProperty("单位税号")
    private String number;

    @ApiModelProperty("抬头")
    private String title;

    @ApiModelProperty("抬头类型")
    private String titleType;

    @ApiModelProperty("删除")
    private String isDel;

    @ApiModelProperty("审核状态 1 未开涵 2待审批 3已开涵 4已退回")
    private String state;

    @ApiModelProperty("开票日期")
    private String invoiceTime;

    @ApiModelProperty("申请开票日期")
    private String createTime;

    @ApiModelProperty("支付类型 1 款已付 2款未付")
    private String payType;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("案件开票1/顾问客户开票2")
    private String caseOrCustomer;
}
