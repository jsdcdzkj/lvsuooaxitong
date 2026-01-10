package com.jsdc.lsoa.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ApiModel("当事人信息实体")
@TableName("client_info")
@Table(name = "client_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo extends Model<ClientInfo> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("1自然人 2法人 3个体工商户 4受害人 5协议客户")
    private String isPerson;

    @ApiModelProperty("案件ID（关联案件表）")
    private String caseId;

    @ApiModelProperty("姓名/公司名")
    private String name;

    @ApiModelProperty("性别（1男 2女）")
    private String sex;

    @ApiModelProperty("身份证号/统一代码")
    private String code;

    @ApiModelProperty("委托人与对方委托人 1：委托人 2：对方委托人")
    private String plainOrDefend;

    @ApiModelProperty("原告与被告 1：原告 2：被告 3犯罪嫌疑人 4第三人（非原被告）5：受害人")
    private String pod;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("删除标记（0正常 1删除）")
    private Integer isDel;

    @ApiModelProperty("协约客户id")
    private String customerId;

}
