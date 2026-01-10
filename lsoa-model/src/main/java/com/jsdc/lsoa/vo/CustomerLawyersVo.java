package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.CustomerLawyers;
import lombok.Data;

@Data
public class CustomerLawyersVo extends CustomerLawyers {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
