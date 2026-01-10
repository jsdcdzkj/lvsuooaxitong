package com.jsdc.lsoa.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.core.common.utils.DateUtil;
import com.jsdc.lsoa.mapper.SysPostMapper;
import com.jsdc.lsoa.mapper.SysUserPostMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysPostVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * SysUserService
 *
 * @author 许森森
 * @date 2024/12/19
 */

@Slf4j
@Service
public class SysPostService extends BaseService<SysPost> {

    @Autowired
    SysPostMapper mapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserService userService;

    public Page getPage(SysPostVo vo){
        LambdaQueryWrapper<SysPost> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysPost::getIsDel,0).like(StringUtils.isNotBlank(vo.getName())
                ,SysPost::getName,vo.getName()).
                between(StringUtils.isNotBlank(vo.getStart()),SysPost::getCreateTime,
                        DateUtil.parseDate(vo.getStart()+" 00:00:00"),DateUtil.parseDate(vo.getEnd()+" 23:59:59")).like(StringUtils.isNotBlank(vo.getPostCode()),SysPost::getPostCode,
                vo.getPostCode()).orderByDesc(SysPost::getCreateTime);
        Page<SysPost>  page = mapper.selectPage(new Page<>(vo.getPageIndex(),vo.getPageSize()), wrapper);
        return  page;
    }


    public ResultInfo saveOrUpdatePost(SysPostVo vo){
        LambdaQueryWrapper<SysPost> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysPost::getIsDel,0).and(sysDeptLambdaQueryWrapper -> {sysDeptLambdaQueryWrapper. eq(SysPost::getName,vo.getName()).or().eq(SysPost::getPostCode,vo.getPostCode());
        });
        List<SysPost> sysPosts = mapper.selectList(wrapper);
        if (!sysPosts.isEmpty()&&Objects.isNull(vo.getId())){
            return ResultInfo.error("名称或编码重复");
        }
        SysUser user = userService.getUser();
        SysPost bean = new SysPost();
        BeanUtils.copyProperties(vo, bean);
        if (Objects.isNull(vo.getParentId())) {
            bean.setParentId(0);
        }

        if (Objects.isNull(bean.getId())){
            bean.setCreateTime(new Date());
            bean.setIsDel(0);
            bean.setCreateUser(user.getId());
        }else {
            bean.setUpdateTime(new Date());
            bean.setUpdateUser(user.getId());
        }
        saveOrUpdate(bean);
        return ResultInfo.success();
    }


    @Transactional(rollbackFor = Exception.class)
    public ResultInfo del(SysPostVo bean){
        SysUser user = userService.getUser();
        SysPost post = mapper.selectById(bean.getId());
        LambdaQueryWrapper<SysPost> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysPost::getIsDel,0).eq(SysPost::getParentId,bean.getId());
        List<SysPost> sysPosts = mapper.selectList(wrapper);
        if (sysPosts.size()>0){
            return ResultInfo.error("有子岗位无法删除");
        }
        Date now = new Date();
        post.setIsDel(1);
        post.setUpdateTime(now);
        post.setUpdateUser(user.getId());
        updateById(post);

        List<SysUserPost> list = userPostMapper.selectList(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getIsDel, 0)
                .eq(SysUserPost::getPostId, post.getId())
        );
        for (SysUserPost userPost : list) {
            userPost.setIsDel(1);
            userPost.setUpdateTime(now);
            userPost.setUpdateUser(user.getId());
            userPostMapper.updateById(userPost);
        }

        return  ResultInfo.success(post);
    }


    public SysPost getSysPost(SysPostVo bean){
        SysPost post = getById(bean.getId());
        return  post;
    }

    public void updateUserPost(Integer userId, Integer mainPost, List<Integer> deputyPost, Integer loginUser) {
        Date now = new Date();
        SysUser user = userService.getById(userId);
        // 主岗
        SysUserPost main = userPostMapper.selectOne(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getIsDel, 0)
                .eq(SysUserPost::getUserId, user.getId())
                .eq(SysUserPost::getType, 1)
        );
        if (Objects.isNull(mainPost)) {
            if (Objects.nonNull(main)) {
                main.setIsDel(1);
                userPostMapper.updateById(main);
            }
        }else {
            SysUserPost entity = new SysUserPost();
            entity.setUserId(user.getId());
            entity.setPostId(mainPost);
            entity.setType(1);
            entity.setIsDel(0);
            entity.setUpdateUser(loginUser);
            entity.setUpdateTime(now);
            if (Objects.isNull(main)) {
                entity.setCreateUser(loginUser);
                entity.setCreateTime(now);
                userPostMapper.insert(entity);
            }else {
                entity.setId(main.getId());
                userPostMapper.updateById(entity);
            }
        }
        // 副岗
        List<SysUserPost> oldList = userPostMapper.selectList(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getIsDel, 0)
                .eq(SysUserPost::getUserId, user.getId())
                .eq(SysUserPost::getType, 2)
        );
        if (CollUtil.isEmpty(deputyPost)) {
            if (CollUtil.isNotEmpty(oldList)) {
                for (SysUserPost post : oldList) {
                    post.setIsDel(1);
                    userPostMapper.updateById(post);
                }
            }
        }else {
            if (CollUtil.isNotEmpty(oldList)) {
                List<Integer> newIds = new ArrayList<>(deputyPost);
                List<Integer> temp = new ArrayList<>(deputyPost);
                List<Integer> oldIds = oldList.stream().map(SysUserPost::getPostId).collect(Collectors.toList());
                newIds.removeAll(oldIds);
                if (CollUtil.isNotEmpty(newIds)) {
                    for (Integer id : newIds) {
                        SysUserPost item = new SysUserPost();
                        item.setUserId(user.getId());
                        item.setPostId(id);
                        item.setType(2);
                        item.setIsDel(0);
                        item.setUpdateUser(loginUser);
                        item.setUpdateTime(now);
                        item.setCreateUser(loginUser);
                        item.setCreateTime(now);
                        userPostMapper.insert(item);
                    }
                }
                oldIds.removeAll(temp);
                if (CollUtil.isNotEmpty(oldIds)) {
                    userPostMapper.update(null, new LambdaUpdateWrapper<SysUserPost>()
                            .eq(SysUserPost::getIsDel, 0)
                            .eq(SysUserPost::getUserId, user.getId())
                            .in(SysUserPost::getPostId, oldIds)
                            .set(SysUserPost::getIsDel, 1)
                            .set(SysUserPost::getUpdateUser, loginUser)
                            .set(SysUserPost::getUpdateTime, now)
                    );
                }

            }else {
                for (Integer id : deputyPost) {
                    SysPost post = mapper.selectById(id);
                    SysUserPost item = new SysUserPost();
                    item.setUserId(user.getId());
                    item.setPostId(id);
                    item.setType(2);
                    item.setIsDel(0);
                    item.setUpdateUser(loginUser);
                    item.setUpdateTime(now);
                    item.setCreateUser(loginUser);
                    item.setCreateTime(now);
                    userPostMapper.insert(item);
                }
            }
        }
    }



    public List<SysPost> getSysPostList(SysPostVo bean){
        LambdaQueryWrapper<SysPost> wrapper=new LambdaQueryWrapper();
        wrapper.eq(SysPost::getIsDel,0).eq(Objects.nonNull(bean.getDeptId()),SysPost::getDeptId,
                bean.getDeptId()).like(StringUtils.isNotEmpty(bean.getName())
                ,SysPost::getName,bean.getName()).
                between(StringUtils.isNotEmpty(bean.getStart()),SysPost::getCreateTime,
                        DateUtil.parseDate(bean.getStart()+" 00:00:00"),
                        DateUtil.parseDate(bean.getEnd()+" 23:59:59")).like(StringUtils.isNotEmpty(bean.getPostCode())
                ,SysPost::getPostCode,
                bean.getPostCode()).orderByDesc(SysPost::getCreateTime);
        List<SysPost> sysPosts = mapper.selectList(wrapper);
        return  sysPosts;
    }

    public List<SysPost> selectByUser(Integer userId, int type) {
        List<SysUserPost> list = userPostMapper.selectList(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getIsDel, 0)
                .eq(SysUserPost::getUserId, userId)
                .eq(SysUserPost::getType, type)
        );
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<Integer> collect = list.stream().map(SysUserPost::getPostId).collect(Collectors.toList());
        return mapper.selectList(new LambdaQueryWrapper<SysPost>().eq(SysPost::getIsDel, 0).in(SysPost::getId, collect));
    }

    public SysPost getById(Integer id) {
        return mapper.selectById(id);
    }

}
