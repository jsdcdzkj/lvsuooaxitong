package com.jsdc.lsoa.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalVo {

    private String caseType;
    private String userId;
    private String startTime;
    private String endTime;
    private String flag; // day 自定义时间 month 月 year 年
    private String caseTypeName;
    private String time; // 月或年的时间 某个月（格式：yyyy-MM） 某年（格式：yyyy）
    private String caseCount;
    private String totalMoney;
}
