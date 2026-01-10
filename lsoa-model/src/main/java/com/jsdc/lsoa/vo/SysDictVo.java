package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.SysDict;
import lombok.Data;

@Data
public class SysDictVo extends SysDict {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
