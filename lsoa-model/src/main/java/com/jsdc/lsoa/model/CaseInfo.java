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
@TableName("case_info")
@Table(name = "case_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseInfo extends Model<CaseInfo> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("案件号（UUID）")
    private String caseNum;

    @ApiModelProperty("案件名称")
    private String caseName;

    @ApiModelProperty("案件类型（1刑事案件 2民事案件 3非诉讼法律师事务 4行政案件 、5破产与重组案件、6法律援助案件）")
    private String caseType;

    @Transient
    @TableField(exist = false)
    private String caseTypeStr;

    @ApiModelProperty("业务类型（1婚姻家事 2公司案件 3金融银行案件 4证券纠纷 5保险纠纷 6医疗纠纷 7建设工程与房地产案件 8知识产权案件 9劳动争议案件 10行政案件 11涉外法律服务 " +
            "12破产重组案件 13其他）")
    private String businessType;
    @Transient
    @TableField(exist = false)
    private String businessTypeStr;

    @ApiModelProperty("案件简介")
    private String caseDesc;

    @ApiModelProperty("费用")
    private String money;

    @ApiModelProperty("诉讼阶段（1一审 2二审）4侦查，5审查起诉，3再审")
    private String stage;

    @Transient
    @TableField(exist = false)
    private String stageStr;

    @ApiModelProperty("案件状态（1进行中 2已完结 3已中止（解除委托或撤销））")
    private String status;

    @Transient
    @TableField(exist = false)
    private String statusStr;

    @ApiModelProperty("标的额")
    private String subjectAmount;

    @ApiModelProperty("开票状态（1未开票 2待审批 3已开票 4已退回）")
    private String invoice;

    @Transient
    @TableField(exist = false)
    private String invoiceStr;

    @ApiModelProperty("开涵状态（1未开函 2待审批 3已开函 4已退回）")
    private String letter;

    @ApiModelProperty("开票退回")
    private String invoiceBack;

    @ApiModelProperty("开函退回")
    private String letterBack;

    @Transient
    @TableField(exist = false)
    private String letterStr;

    @ApiModelProperty("登记人")
    private String createUser;

    @Transient
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("登记时间")
    private String createTime;

    @ApiModelProperty("修改人")
    private String updateUser;

    @ApiModelProperty("修改时间")
    private String updateTime;

    @ApiModelProperty("删除标记（0正常 1删除）")
    private Integer isDel;

    @ApiModelProperty("当事人（逗号分隔）")
    private String client;

    @ApiModelProperty("对方当事人（逗号分隔）")
    private String tarClient;

    @ApiModelProperty("部门ID")
    private String deptId;
    // 1 未归档 2 已归档
    @ApiModelProperty("归档状态")
    private String archived;

    @ApiModelProperty("协约客户id")
    private String customerId;

    @ApiModelProperty("管辖法院")
    private String court;

    @ApiModelProperty("案由")
    private String subjectOfAction;

    @ApiModelProperty("案件提交状态 1暂存 2提交")
    private String temporarily;

    @ApiModelProperty("风险代理/费用减免 1风险代理 2费用减免")
    private String riskOrFree;

    @ApiModelProperty("开票json")
    private String invoiceJson;

    @ApiModelProperty("开函json")
    private String summonsJson;

    @ApiModelProperty("委托方式 1顾问单位 2非顾问单位")
    private String delegate;
}
