package com.jsdc.lsoa.vo;


import com.jsdc.lsoa.model.SysDept;
import lombok.Data;

@Data
public class SysDeptVo extends SysDept {

    private Integer pageIndex =1;

    private Integer pageSize=10;

    private String start;

    private String end;
}
