package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.ProcessApproveRecord;
import com.jsdc.lsoa.model.SysFile;
import com.jsdc.lsoa.model.SysRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class InvoiceInfoVo extends InvoiceInfo {
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    private String approveStatus;//1同意   2退回

    private MultipartFile[] files;


    private List<SysFile> fileList;

    private String back;//退回内容

    private String startTime;
    private String endTime;
    private ProcessApproveRecord approveIds;//审批人id

    private String userName;

    private CaseInfoVo info;//案件详情

    private AgreementCustomerVO cust;//案件详情

    private String numORname;
    private String approveUser;
    private List<SysRole> approveRole;
    private String client;
    private String caseType;
    private String invoice;
    private String letter;
    private String invoiceTypeName;
    private List<SysFile> caseFiles;

}
