package com.jsdc.lsoa.vo;


import com.jsdc.lsoa.model.SysMenu;
import lombok.Data;


@Data
public class SysMenuVo  extends SysMenu {

    private Integer pageIndex = 1;

    private Integer pageSize =10;

    private String start;

    private String end;

}
