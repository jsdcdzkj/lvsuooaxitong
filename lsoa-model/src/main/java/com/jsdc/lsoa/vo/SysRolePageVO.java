package com.jsdc.lsoa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRolePageVO extends PageVO{

    @ApiModelProperty(value = "角色名")
    private String roleName;

}
