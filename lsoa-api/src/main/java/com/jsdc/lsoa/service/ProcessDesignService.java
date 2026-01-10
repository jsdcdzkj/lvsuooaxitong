package com.jsdc.lsoa.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.mapper.ProcessDesignMapper;
import com.jsdc.lsoa.mapper.SysUserRoleMapper;
import com.jsdc.lsoa.model.ProcessDesign;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessDesignService {


    @Autowired
    private ProcessDesignMapper processDesignMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    public List<ProcessDesign> list() {
        List<ProcessDesign> list = processDesignMapper.selectList(new LambdaQueryWrapper<>());
        List<SysUser> userList = userService.list();
        Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealName));
        List<SysRole> roleList = roleService.list();
        Map<Integer, String> roleMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName));
        for (ProcessDesign design : list) {
            String sponsorValue = design.getSponsorValue();
            String[] idArray = sponsorValue.split(",");
            List<String> nameList = new ArrayList<>();
            if (Objects.equals(design.getSponsorType(), 1)) {
                for (String id : idArray) {
                    nameList.add(userMap.get(Integer.parseInt(id)));
                }
            }else {
                for (String id : idArray) {
                    nameList.add(roleMap.get(Integer.parseInt(id)));
                }
            }
            design.setSponsorName(String.join("，", nameList));
        }
        return list;
    }


    public ProcessDesign getById(Integer id) {
        return processDesignMapper.selectById(id);
    }

    public void save(ProcessDesign entity) {
        SysUser loginUser = userService.getUser();
        Date now = new Date();

        entity.setUpdateTime(now);
        entity.setUpdateUser(loginUser.getId());
        // 新增
        if (Objects.isNull(entity.getId())) {
            entity.setIsEnable("Y");
            entity.setCreateTime(now);
            entity.setCreateUser(loginUser.getId());
            processDesignMapper.insert(entity);
        }else {
            processDesignMapper.updateById(entity);
        }
    }

    public ProcessDesign toInvoice(String id) {
        SysUser user = userService.getUser();
        List<SysRole> list = sysUserRoleMapper.getEntityList(user.getId() + "");
        user.setRoleList(list);
        return processDesignMapper.toInvoice(id,user);
    }

}
