package com.jsdc.lsoa.vo;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.lsoa.model.AgreementCustomer;
import com.jsdc.lsoa.model.InvoiceInfo;
import com.jsdc.lsoa.model.SysFile;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AgreementCustomerVO extends AgreementCustomer {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private BigDecimal feeStart;

    private BigDecimal feeEnd;

    private List<AgreementCustomer> acList;

    private List<Integer> fileIdList;

    private List<SysFile> files;

    private List<Integer> lawyers;

    private String lawyerStr;

    private String lawyerNames;

    private List<String> delIds;

    private Integer userId;

    private Integer lawyer;

    private String invoiceNum;
}

