package com.jsdc.lsoa.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@TableName("summons_info")
@Table(name = "summons_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummonsInfo extends Model<SummonsInfo> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("案件id")
    private String caseId;

    @ApiModelProperty("发票内容")
    private String content;

    @ApiModelProperty("委托人类型")
    private String personType;

    @ApiModelProperty("单位名称/个人名称")
    private String name;

    @ApiModelProperty("统一代码/身份证号")
    private String code;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("开函类型")
    private String type;

    @ApiModelProperty("函件类型")
    private String correspondenceType;

    @ApiModelProperty("相关案件")
    private String caseName;

    @ApiModelProperty("事项编号")
    private String caseCode;

    @ApiModelProperty("用印类型")
    private String printType;

    @ApiModelProperty("用途")
    private String useing;

    @ApiModelProperty("收件方")
    private String addressType;

    @ApiModelProperty("收件单位名称")
    private String addressee;

    @ApiModelProperty("收件地址")
    private String address;

    @ApiModelProperty("收件人电话")
    private String addresseePhone;

    @ApiModelProperty("特殊格式要求")
    private String specialDemand;

    @ApiModelProperty("紧急程度")
    private String level;

    @ApiModelProperty("份数")
    private String num;

    @ApiModelProperty("送达方式")
    private String sendType;

    @ApiModelProperty("删除")
    private String isDel;

    @ApiModelProperty("开函日期")
    private String summonsTime;

    @ApiModelProperty("申请开函日期")
    private String createTime;

    @ApiModelProperty("审核状态")
    private String state;

    @Transient
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("登记人")
    private String createUser;
}
