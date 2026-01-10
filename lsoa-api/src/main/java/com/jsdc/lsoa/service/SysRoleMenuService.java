package com.jsdc.lsoa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.mapper.*;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.SysRoleMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SysUserService
 *
 * @author 许森森
 * @date 2024/12/19
 */

@Slf4j
@Service
public class SysRoleMenuService extends BaseService<SysRoleMenu> {

    @Autowired
    SysRoleMenuMapper mapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;



    public  void saveRoleMenu(SysRoleMenuVo bean) {
        SysUser user = userService.getUser();
        SysRole sysRole = roleMapper.selectById(bean.getRoleId());
        List<String> menuIds = Arrays.asList(bean.getMenuIds());
        LambdaQueryWrapper<SysRoleMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,bean.getRoleId());
        mapper.update(new SysRoleMenu().setIsDel(1),wrapper);
        for (String menuId : menuIds) {
            SysMenu menu = menuMapper.selectById(menuId);
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());
            sysRoleMenu.setMenuId(menu.getId());
            sysRoleMenu.setIsDel(0);
            sysRoleMenu.setCreateUser(user.getId());
            sysRoleMenu.setUpdateUser(user.getId());
            sysRoleMenu.setCreateTime(new Date());
            sysRoleMenu.setUpdateTime(new Date());

            mapper.insert(sysRoleMenu);
        }
    }

    public List<SysRoleMenu> getSysRoleMenuList(SysRoleMenuVo bean){
        LambdaQueryWrapper<SysRoleMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,bean.getRoleId()).eq(SysRoleMenu::getIsDel,0);
        List<SysRoleMenu> list = list(wrapper);
        return list;
    }


}
