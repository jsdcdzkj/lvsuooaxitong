package com.jsdc.lsoa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.core.common.utils.DateUtil;
import com.jsdc.lsoa.mapper.SysMenuMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * SysUserService
 *
 * @author 许森森
 * @date 2024/12/19
 */

@Slf4j
@Service
public class SysMenuService extends BaseService<SysMenu> {

    @Autowired
    SysMenuMapper mapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleMenuService roleMenuService;

    public Page getPage(SysMenuVo vo){
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysMenu::getIsDel,0).like(Objects.nonNull(vo.getTitle()),SysMenu::getTitle,vo.getTitle()).
                between(StringUtils.isNotBlank(vo.getStart()),SysMenu::getCreateTime,DateUtil.parseDate(vo.getStart()+" 00:00:00")
                        ,DateUtil.parseDate(vo.getEnd()+" 23:59:59")).orderByAsc(SysMenu::getSort);
        Page<SysMenu>  page = mapper.selectPage(new Page<>(vo.getPageIndex(),vo.getPageSize()), wrapper);
        return  page;
    }


    public void saveOrUpdateMenu(SysMenuVo vo){
        SysUser user = userService.getUser();
        SysMenu bean = new SysMenu();
        BeanUtils.copyProperties(vo, bean);
        if (Objects.isNull(bean.getId())){
            bean.setCreateTime(new Date());
            bean.setIsDel(0);
            bean.setCreateUser(user.getId());
            bean.setUpdateUser(user.getId());
        }else {
            bean.setUpdateTime(new Date());
            bean.setUpdateUser(user.getId());
        }
        this.saveOrUpdate(bean);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResultInfo del(SysMenuVo bean){
        SysUser user = userService.getUser();
        SysMenu menu = mapper.selectById(bean.getId());
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysMenu::getIsDel,0).eq(SysMenu::getParentId,bean.getId());
        List<SysMenu> sysMenus = mapper.selectList(wrapper);
        if (sysMenus.size()>0){
            return ResultInfo.error("有子菜单无法删除");
        }
        Date now = new Date();
        menu.setIsDel(1);
        menu.setUpdateTime(now);
        menu.setUpdateUser(user.getId());
        updateById(menu);

        roleMenuService.update(new LambdaUpdateWrapper<SysRoleMenu>()
                .set(SysRoleMenu::getIsDel, 1)
                .set(SysRoleMenu::getUpdateTime, now)
                .set(SysRoleMenu::getUpdateUser, user.getId())
                .eq(SysRoleMenu::getIsDel, 0)
                .eq(SysRoleMenu::getMenuId, menu.getId())
        );
        return  ResultInfo.success(menu);
    }


    public SysMenu getSysMenu(SysMenuVo bean){
        SysMenu menu = getById(bean.getId());
        return  menu;
    }

    public List<SysMenu> getSysMenuTree(SysMenuVo bean){
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysMenu::getIsDel,0).eq(SysMenu::getParentId,0)
                .like(StringUtils.isNotEmpty(bean.getTitle()),SysMenu::getTitle,bean.getTitle()).
                between(StringUtils.isNotEmpty(bean.getStart()),SysMenu::getCreateTime,
                        DateUtil.parseDate(bean.getStart()),DateUtil.parseDate(bean.getEnd())).orderByAsc(SysMenu::getSort);
        List<SysMenu> sysMenus = mapper.selectList(wrapper);
        return  getMenu(sysMenus);
    }

    public List<SysMenu> getMenu(List<SysMenu> sysMenus){
        for (SysMenu sysMenu : sysMenus) {
                LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper();
                wrapper.eq(SysMenu::getIsDel,0).eq(SysMenu::getParentId,sysMenu.getId()).orderByAsc(SysMenu::getSort);
                List<SysMenu> list = mapper.selectList(wrapper);
                getMenu(list);
                sysMenu.setMenuChild(list);
        }
        return sysMenus;
    }

    public List<SysMenu> getSysMenuList(SysMenuVo bean){
        LambdaQueryWrapper<SysMenu> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysMenu::getIsDel,0).like(StringUtils.isNotEmpty(bean.getTitle()),SysMenu::getTitle,bean.getTitle()).
                between(StringUtils.isNotEmpty(bean.getStart()),SysMenu::getCreateTime,
                        DateUtil.parseDate(bean.getStart()),DateUtil.parseDate(bean.getEnd())).orderByAsc(SysMenu::getSort);
        List<SysMenu> sysMenus = mapper.selectList(wrapper);
        return  sysMenus;
    }

}
