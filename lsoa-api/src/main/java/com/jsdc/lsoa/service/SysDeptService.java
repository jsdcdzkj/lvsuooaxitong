package com.jsdc.lsoa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.core.common.utils.DateUtil;
import com.jsdc.lsoa.mapper.SysDeptMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
public class SysDeptService extends BaseService<SysDept> {

    @Autowired
    SysDeptMapper mapper;

    @Autowired
    private SysUserService userService;

    public Page getPage(SysDeptVo vo){
        LambdaQueryWrapper<SysDept> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysDept::getIsDel,0).like(StringUtils.isNotEmpty(vo.getName()),SysDept::getName,vo.getName()).
                between(StringUtils.isNotEmpty(vo.getStart()), SysDept::getCreateTime,
                        DateUtil.parseDate(vo.getStart()+" 00:00:00"),DateUtil.parseDate(vo.getEnd()+" 23:59:59")).orderByDesc(SysDept::getCreateTime);
        Page<SysDept>  page = mapper.selectPage(new Page<>(vo.getPageIndex(),vo.getPageSize()), wrapper);
        return  page;
    }


    public ResultInfo saveOrUpdateDept(SysDeptVo vo){
        LambdaQueryWrapper<SysDept> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysDept::getIsDel,0).and(sysDeptLambdaQueryWrapper -> {sysDeptLambdaQueryWrapper.eq(SysDept::getName,vo.getName());
        });
        List<SysDept> sysDepts = mapper.selectList(wrapper);
        if (sysDepts.size()>0 && Objects.isNull(vo.getId())){
            return ResultInfo.error("名称或编码重复");
        }
        SysUser user = userService.getUser();
        SysDept bean = new SysDept();
        BeanUtils.copyProperties(vo, bean);
        if (Objects.isNull(bean.getId())){
            bean.setCreateTime(new Date());
            bean.setIsDel(0);
            bean.setCreateUser(user.getId());
            bean.setUpdateUser(user.getId());
        }else {
            if (Objects.isNull(vo.getParentId())) {
                bean.setParentId(0);
            }
            bean.setUpdateTime(new Date());
            bean.setUpdateUser(user.getId());
        }
        this.saveOrUpdate(bean);
        return ResultInfo.success(bean);
    }


    public ResultInfo del(SysDeptVo bean){
        SysUser user = userService.getUser();
        SysDept dept = mapper.selectById(bean.getId());
        LambdaQueryWrapper<SysDept> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysDept::getIsDel,0).eq(SysDept::getParentId,bean.getId());
        List<SysDept> sysDepts = mapper.selectList(wrapper);
        if (sysDepts.size()>0){
            return ResultInfo.error("有子部门无法删除");
        }
        Date now = new Date();
        dept.setIsDel(1);
        dept.setUpdateTime(now);
        dept.setUpdateUser(user.getId());
        updateById(dept);
        return  ResultInfo.success(dept);
    }


    public SysDept getSysDept(SysDeptVo bean){
        SysDept dept = getById(bean.getId());
        return  dept;
    }

    public List<SysDept> getSysDeptList(SysDeptVo bean){
        LambdaQueryWrapper<SysDept> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysDept::getIsDel,0).eq(Objects.nonNull(bean.getParentId()),SysDept::getParentId,
                bean.getParentId()).like(StringUtils.isNotEmpty(bean.getName()),SysDept::getName,bean.getName()).
                between(StringUtils.isNotEmpty(bean.getStart()),SysDept::getCreateTime,
                        DateUtil.parseDate(bean.getStart()+" 00:00:00"),
                        DateUtil.parseDate(bean.getEnd())+" 23:59:59").orderByDesc(SysDept::getCreateTime);
        List<SysDept> sysDepts = mapper.selectList(wrapper);
        return  sysDepts;
    }

    public SysDept getById(Integer id) {
        return mapper.selectById(id);
    }
}
