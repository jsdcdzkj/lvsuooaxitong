package com.jsdc.lsoa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysSystemPageVO extends PageVO{

    @ApiModelProperty(value = "系统名称")
    private String name;

    @ApiModelProperty(value = "系统编码，唯一标识")
    private String code;

}
