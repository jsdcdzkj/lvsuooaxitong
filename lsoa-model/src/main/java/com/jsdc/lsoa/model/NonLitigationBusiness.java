package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@TableName("non_litigation_business")
@Table(name = "non_litigation_business")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonLitigationBusiness implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("非诉编号")
    private String code;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("法人")
    private String legalPerson;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("社会统一信用代码")
    private String companyCode;

    @ApiModelProperty("律师Id（英文逗号分隔）")
    private String lawyerId;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty("律师名称")
    private String lawyerName;

    @ApiModelProperty("非诉讼业务类型（字典：NonLitigationBusiness）")
    private Integer businessType;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty("非诉讼业务名称")
    private String businessName;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty("合同文件")
    private List<SysFile> contractFile;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty("发票文件")
    private List<SysFile> invoiceFile;

    @ApiModelProperty("审批状态（0：草稿，1：未审核，2：审核通过，3：审核不通过）")
    private Integer status;

    @ApiModelProperty(value = "审批人")
    private Integer checkUser;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "审批人名称")
    private Integer checkUserName;

    @ApiModelProperty(value = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;

    @ApiModelProperty(value = "审批结果（1：通过，2：不通过）")
    private Integer checkResult;

    @ApiModelProperty(value = "审批备注")
    private String checkDesc;

    @ApiModelProperty("删除标记（0正常 1删除）")
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

}
