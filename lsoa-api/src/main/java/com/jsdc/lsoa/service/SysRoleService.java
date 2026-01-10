package com.jsdc.lsoa.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.ProcessNodeMapper;
import com.jsdc.lsoa.mapper.SysRoleMapper;
import com.jsdc.lsoa.mapper.SysRoleMenuMapper;
import com.jsdc.lsoa.mapper.SysUserRoleMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.SysRolePageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private ProcessNodeMapper processNodeMapper;

    public void updateUserRole(Integer userId, List<Integer> roleIds, Integer loginUserId) {
        Date now = new Date();

        userRoleMapper.update(null, new LambdaUpdateWrapper<SysUserRole>()
                .set(SysUserRole::getIsDel, 1)
                .set(SysUserRole::getUpdateUser, loginUserId)
                .set(SysUserRole::getUpdateTime, now)
                .eq(SysUserRole::getIsDel, 0)
                .eq(SysUserRole::getUserId, userId)
        );

        for (Integer roleId : roleIds) {
            SysRole role = roleMapper.selectById(roleId);

            SysUserRole entity = new SysUserRole();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            entity.setIsDel(0);
            entity.setCreateUser(loginUserId);
            entity.setUpdateUser(loginUserId);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            userRoleMapper.insert(entity);
        }

    }


    public Page<SysRole> page(SysRolePageVO vo) {
        Page<SysRole> page = new Page<>(vo.getPageIndex(), vo.getPageSize());
        Page<SysRole> result = roleMapper.selectPage(page, new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getIsDel, 0)
                .like(StringUtils.isNotBlank(vo.getRoleName()), SysRole::getRoleName, vo.getRoleName())
        );
        return result;
    }

    public List<SysRole> list() {
        List<SysRole> list = roleMapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getIsDel, 0));
        return list;
    }

    public void save(SysRole entity) {
        Integer loginUserId = userService.getUser().getId();
        Date now = new Date();

        if (StringUtils.isBlank(entity.getRoleName())) {
            throw new CustomException("缺少必要参数");
        }

        SysRole selected = roleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getIsDel, 0).eq(SysRole::getRoleName, entity.getRoleName()));

        entity.setIsDel(0);
        entity.setUpdateUser(loginUserId);
        entity.setUpdateTime(now);
        if (Objects.isNull(entity.getId())) {
            if (Objects.nonNull(selected)) {
                throw new CustomException("角色名已存在");
            }
            entity.setCreateUser(loginUserId);
            entity.setCreateTime(now);
            roleMapper.insert(entity);
        }else {
            if (Objects.nonNull(selected) && !Objects.equals(entity.getId(), selected.getId())) {
                throw new CustomException("角色名已存在");
            }
            roleMapper.updateById(entity);
        }
    }

    public void delete(SysRole entity) {
        Integer loginUser = userService.getUser().getId();
        Date now = new Date();
        if (Objects.isNull(entity.getId())) {
            throw new CustomException("请求参数错误");
        }
        List<SysUserRole> list = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getIsDel, 0).eq(SysUserRole::getRoleId, entity.getId()));
        if (CollUtil.isNotEmpty(list)) {
            throw new CustomException("该角色已关联用户，不可删除");
        }
        List<ProcessNode> nodeList = processNodeMapper.selectByApprover(2, entity.getId());
        if (CollUtil.isNotEmpty(nodeList)) {
            throw new CustomException("该角色已绑定审批流程，不可删除");
        }
        entity.setIsDel(1);
        entity.setUpdateUser(loginUser);
        entity.setUpdateTime(now);
        roleMapper.updateById(entity);


        List<SysRoleMenu> menuList = roleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getIsDel, 0).eq(SysRoleMenu::getRoleId, entity.getId()));
        if (CollUtil.isNotEmpty(menuList)) {
            List<Integer> collect = menuList.stream().map(SysRoleMenu::getRoleId).collect(Collectors.toList());
            roleMenuMapper.update(null, new LambdaUpdateWrapper<SysRoleMenu>()
                    .set(SysRoleMenu::getIsDel, 1)
                    .set(SysRoleMenu::getUpdateUser, loginUser)
                    .set(SysRoleMenu::getUpdateTime, now)
                    .in(SysRoleMenu::getId, collect)
            );
        }

    }
    public List<SysUser> getNormalUserList(){
        return userRoleMapper.getNormalUserList();
    }

}
