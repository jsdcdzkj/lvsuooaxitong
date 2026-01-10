package com.jsdc.lsoa.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.*;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.utils.MD5Utils;
import com.jsdc.lsoa.utils.PasswordUtil;
import com.jsdc.lsoa.utils.RedisUtils;
import com.jsdc.lsoa.vo.SysUserPageVO;
import com.jsdc.lsoa.vo.SysUserPwdVO;
import com.jsdc.lsoa.vo.SysUserSaveVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Lazy
    @Autowired
    private SysRoleService roleService;

    @Lazy
    @Autowired
    private SysPostService postService;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private ProcessNodeMapper processNodeMapper;


    private static final String DEFAULT_PASSWORD = "Jsdc@0516";

    public SysUser getUser() {
        SysUser user = (SysUser) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
        user = userMapper.selectById(user.getId());
        List<SysUserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getIsDel, 0)
                .eq(SysUserRole::getUserId, user.getId())
                .orderByAsc(SysUserRole::getId)
        );
        List<Integer> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> roleList = new ArrayList<>();
        for (Integer roleId : roleIds) {
            roleList.add(roleMapper.selectById(roleId));
        }
        if (Objects.nonNull(user.getDeptId())) {
            SysDept dept = deptMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getIsDel, 0).eq(SysDept::getId, user.getDeptId()));
            user.setDept(dept);
        }
        user.setRoleList(roleList);
        return user;
    }

    public SysUser getById(Integer id) {
        return userMapper.selectById(id);
    }


    public Page<SysUser> page(SysUserPageVO vo) {
        Page<SysUser> page = new Page<>(vo.getPageIndex(), vo.getPageSize());
        Page<SysUser> result = userMapper.getPage(page, vo);
//        Page<SysUser> result = userMapper.selectPage(page, new LambdaQueryWrapper<SysUser>()
//                .eq(SysUser::getIsDel, 0)
//                .eq(Objects.nonNull(vo.getStatus()), SysUser::getStatus, vo.getStatus())
//                .eq(Objects.nonNull(vo.getDeptId()), SysUser::getDeptId, vo.getDeptId())
//                .like(StringUtils.isNotBlank(vo.getWorkId()), SysUser::getWorkId, vo.getWorkId())
//                .like(StringUtils.isNotBlank(vo.getPhone()), SysUser::getPhone, vo.getPhone())
//                .like(StringUtils.isNotBlank(vo.getRealName()), SysUser::getRealName, vo.getRealName())
//                .orderByAsc(SysUser::getId)
//        );
        if (result.getTotal() > 0) {
            for (SysUser user : result.getRecords()) {
                List<SysUserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getIsDel, 0)
                        .eq(SysUserRole::getUserId, user.getId())
                        .orderByAsc(SysUserRole::getId)
                );
                if (CollUtil.isNotEmpty(userRoles)) {
                    List<Integer> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> roleList = new ArrayList<>();
                    for (Integer roleId : roleIds) {
                        roleList.add(roleMapper.selectById(roleId));
                    }
                    user.setRoleList(roleList);
                }
                List<SysPost> mainPosts = postService.selectByUser(user.getId(), 1);
                if (CollUtil.isNotEmpty(mainPosts)) {
                    user.setMainPost(mainPosts.get(0));
                }
                List<SysPost> deputyPosts = postService.selectByUser(user.getId(), 2);
                if (CollUtil.isNotEmpty(deputyPosts)) {
                    user.setDeputyPost(deputyPosts);
                }
                if (Objects.nonNull(user.getDeptId())) {
                    SysDept dept = deptMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getIsDel, 0).eq(SysDept::getId, user.getDeptId()));
                    user.setDept(dept);
                }
            }
        }
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserSaveVO vo) {
        SysUser loginUser = getUser();
        Date now = new Date();
        vo.setNameSpelling(PinyinUtil.getPinyin(vo.getRealName(), ""));
        SysUser selected = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsDel, 0).and(
                        wrapper -> wrapper.eq(SysUser::getRealName, vo.getRealName())
                                .or().
                                eq(SysUser::getNameSpelling, vo.getNameSpelling())
                                .or()
                                .eq(SysUser::getPhone, vo.getPhone())
                )
        );
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(vo, entity);
        entity.setNameSpelling(PinyinUtil.getPinyin(vo.getRealName(), ""));
        entity.setUpdateUser(loginUser.getId());
        entity.setUpdateTime(now);
        entity.setIsDel(0);
        if (Objects.isNull(vo.getId())) {
            if (Objects.nonNull(selected)) {
                throw new CustomException("人员姓名/手机号已存在");
            }
            if (Objects.isNull(entity.getStatus())) {
                entity.setStatus(0);
            }
            entity.setPassword(MD5Utils.getMD5(DEFAULT_PASSWORD));
            entity.setCreateUser(loginUser.getId());
            entity.setCreateTime(now);
            userMapper.insert(entity);
        }else {
            if (Objects.nonNull(selected) && !Objects.equals(vo.getId(), selected.getId())) {
                throw new CustomException("人员姓名/手机号已存在");
            }
            userMapper.updateById(entity);
        }
        if (CollUtil.isNotEmpty(vo.getRoleList())) {
            // 更新用户角色
            roleService.updateUserRole(entity.getId(), vo.getRoleList(), loginUser.getId());
        }
        // 更新用户岗位
//        postService.updateUserPost(entity.getId(), vo.getMainPost(), vo.getDeputyPost(), loginUser.getId(), loginUser.getRealName());
    }

    public void changePassword(SysUserPwdVO vo) {
        SysUser user = getUser();
        Date now = new Date();

        if (StringUtils.isAnyBlank(vo.getPassword(), vo.getConfirmPassword())) {
            throw new CustomException("请求参数错误");
        }
        if (!Objects.equals(vo.getPassword(), vo.getConfirmPassword())) {
            throw new CustomException("两次密码输入不一致");
        }
        String oldPasswordMd5 = MD5Utils.getMD5(vo.getOldPassword());
        if (!Objects.equals(oldPasswordMd5, user.getPassword())) {
            throw new CustomException("原密码输入错误");
        }

        String password = vo.getPassword();
        PasswordUtil.validate(password);
        String md5 = MD5Utils.getMD5(password);
        SysUser entity = new SysUser();
        entity.setId(user.getId());
        entity.setPassword(md5);
        entity.setUpdateUser(user.getId());
        entity.setUpdateTime(now);
        userMapper.updateById(entity);
    }

    public void resetPassword(SysUserPwdVO vo) {
        Integer loginUser = getUser().getId();
        Date now = new Date();

        if (Objects.isNull(vo.getId())) {
            throw new CustomException("请求参数错误");
        }
        if (StringUtils.isAnyBlank(vo.getPassword(), vo.getConfirmPassword())) {
            throw new CustomException("请求参数错误");
        }
        if (!Objects.equals(vo.getPassword(), vo.getConfirmPassword())) {
            throw new CustomException("两次密码输入不一致");
        }

        String password = vo.getPassword();
        PasswordUtil.validate(password);
        String md5 = MD5Utils.getMD5(password);
        SysUser entity = new SysUser();
        entity.setId(vo.getId());
        entity.setPassword(md5);
        entity.setUpdateUser(loginUser);
        entity.setUpdateTime(now);
        userMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(SysUser entity) {
        SysUser loginUser = getUser();
        Date now = new Date();
        if (Objects.isNull(entity.getId())) {
            throw new CustomException("请求参数错误");
        }
        List<ProcessNode> nodeList = processNodeMapper.selectByApprover(1, entity.getId());
        if (CollUtil.isNotEmpty(nodeList)) {
            throw new CustomException("该用户已绑定审批流程，不可删除");
        }
        entity.setIsDel(1);
        entity.setUpdateUser(loginUser.getId());
        entity.setUpdateTime(now);
        userMapper.updateById(entity);

        userRoleMapper.update(null, new LambdaUpdateWrapper<SysUserRole>()
                .set(SysUserRole::getIsDel, 1)
                .eq(SysUserRole::getUserId, entity.getId())
        );
    }

    public String uploadAvatar(MultipartFile file) {
        // 文件存储盘符
        Map<String, String> symbolMap = (Map<String, String>) RedisUtils.getBeanValue(CommonConstants.FILE_DISK_SYMBOL_KEY);
        String symbol = symbolMap.keySet().iterator().next();
        // 文件存储路径
        Map<String, String> pathMap = (Map<String, String>) RedisUtils.getBeanValue(CommonConstants.FILE_DISK_PATH_KEY);
        String path = pathMap.keySet().iterator().next();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        // 文件访问域名
        Map<String, String> domainMap = (Map<String, String>) RedisUtils.getBeanValue(CommonConstants.FILE_DOMAIN_KEY);
        String domain = domainMap.keySet().iterator().next();
        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        List<String> fileTypeList = Arrays.asList(CommonConstants.AVATAR_FILE_TYPE);
        if (!fileTypeList.contains(suffix)) {
            throw new CustomException("只支持上传 " + String.join("，", fileTypeList) + " 格式文件");
        }

        String filename = IdUtil.fastSimpleUUID() + suffix;
        String filePath = symbol + ":/" + path + "/" + filename;
        String fileUrl = domain + "/" + path + "/" + filename;
        try {
            File targetFile = new File(filePath);
            if (!targetFile.getParentFile().exists()) {
                boolean mkdirs = targetFile.getParentFile().mkdirs();
                if (!mkdirs) {
                    throw new CustomException("创建文件夹失败");
                }
            }
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error("文件保存失败：", e);
            throw new CustomException("上传失败");
        }
       return fileUrl;
    }

    public List<SysUser> list() {
        return userMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getIsDel, 0));
    }
}
