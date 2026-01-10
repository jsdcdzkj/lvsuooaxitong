package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CaseInfoVo extends CaseInfo {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private List<ClientInfo> clientInfoVoList;
    private String startTime;
    private String endTime;
    private List<Integer> fileIdList;
    private Map<Integer, List<SysFile>> fileList;
    private List<ClientInfo> plainList; // 委托人
    private List<ClientInfo> defendList;// 对方委托人
    private List<Integer> newFileIdList;
    private List<Integer> delFileIdList;
    private List<Integer> clientDelIds;
    private String currentUserId;
    private ProcessApproveRecord approveIds;//审批人id
    private String approveUser;
    private List<SysRole> approveRole;
    private String approveType;//审批类型 1 全部  2 开票   3 开函
    private String order;//排序 1时间正序  2 时间倒叙
    private String statusList;//状态多选 逗号分隔
    private String userName;//承办律师


    private String numORname;


    //接收查询后开票返回参数
    private String invoiceId;//开票id

    private String content;//开票内容

    private String invoiceMoney;//开票金额

    private String contractMoney;//合同金额

    private String invoiceType;//发票类型

    private String contactPerson;//联系人

    private String contactPhone;//联系人电话

    private String state;//审核状态

    private String invoiceTypeStr;//发票类型名称

    private String mail;//电子邮箱

    private String addressI;//收件地址

    //接收查询开函返回参数
    private String summonsId;//开票id

    private String  invoiceTime;//开票时间\

    private String  applyTime;//申请时间\

    private String payType;//已付未付

    private String processType;

    private String letterType;

    @ApiModelProperty("案件开票1/顾问客户开票2")
    private String caseOrCustomer;

    private String  summonsTime;//开函时间\

    private String addressS;//收件地址

    private String summonsNum;// 开涵数量

    private String invoiceNum;// 开票数量

    private String caseId;//顾问专用

    private SummonsInfoVo summonsInfoVo;
    private InvoiceInfoVo invoiceInfoVo;

    private AgreementCustomerVO cust;//案件详情
}
