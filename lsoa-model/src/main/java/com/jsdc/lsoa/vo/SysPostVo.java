package com.jsdc.lsoa.vo;


import com.jsdc.lsoa.model.SysPost;
import lombok.Data;


@Data
public class SysPostVo extends SysPost {

    private Integer pageIndex=1;

    private Integer pageSize=10;

    private String start;

    private String end;
}
