package com.jsdc.lsoa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.lsoa.model.SysLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SysLogVo extends SysLog {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
