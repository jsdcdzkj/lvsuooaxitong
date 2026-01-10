package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.CaseInfo;
import com.jsdc.lsoa.model.ProcessApproveRecord;
import com.jsdc.lsoa.model.SummonsInfo;
import com.jsdc.lsoa.model.SysFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SummonsInfoVo extends SummonsInfo {
    private Integer pageNo = 1;
    private Integer pageSize = 10;


    private String approveStatus;//1同意   2退回

    private List<String> files;

    private String printTypeName;//用印类型名称

    private List<SysFile> fileList;

    private ProcessApproveRecord approveIds;//审批人id
    private String approveUser;

    private String createUser;

    private CaseInfoVo info;//案件详情

    private String back;//退回内容


    private List<SysFile> caseFiles;
}
