package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.SysRoleMenu;
import lombok.Data;

/**
 * @Author ：苹果
 * @Description：
 * @Date ：2024/12/23 14:46
 * @Modified By：
 */
@Data
public class SysRoleMenuVo extends SysRoleMenu {

    private Integer pageIndex=1;

    private Integer pageSize=10;

    private String[] menuIds;
}
