package com.jsdc.lsoa.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.NonLitigationBusinessMapper;
import com.jsdc.lsoa.mapper.SysFileMapper;
import com.jsdc.lsoa.model.*;
import com.jsdc.lsoa.utils.RedisUtils;
import com.jsdc.lsoa.vo.NonLitigationBusinessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NonLitigationBusinessService {


    @Autowired
    private NonLitigationBusinessMapper nonLitigationBusinessMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysFileMapper fileMapper;

    public Page<NonLitigationBusiness> getPageList(NonLitigationBusinessVO vo) {
        SysUser loginUser = userService.getUser();
        vo.setLoginUser(loginUser.getId());
        List<SysRole> roleList = loginUser.getRoleList();

        Page<NonLitigationBusiness> page = new Page<>(vo.getPageNo(), vo.getPageSize());

        List<String> roleCodes = CollUtil.isEmpty(roleList)
                ? Collections.emptyList()
                : roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

        boolean hasSpecialPermission = Objects.equals(loginUser.getNameSpelling(), "wenquan")
                || CollUtil.isNotEmpty(roleCodes) && (roleCodes.contains("011")
                || roleCodes.contains("admin1"));
        Page<NonLitigationBusiness> selectPage = nonLitigationBusinessMapper.getPage(page, vo, hasSpecialPermission);
        if (selectPage.getTotal() > 0) {
            List<SysUser> userList = userService.list();
            Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealName));

            Map<String, List<SysDict>> dictLists = (Map<String, List<SysDict>>) RedisUtils.getBeanValue(CommonConstants.allDictListPrefix);
            List<SysDict> NonLitigationBusiness = dictLists.get("NonLitigationBusiness");
            Map<String, String> dictMap = NonLitigationBusiness.stream().collect(Collectors.toMap(SysDict::getDictValue, SysDict::getDictLabel));

            for (NonLitigationBusiness record : selectPage.getRecords()) {
                if (StrUtil.isNotBlank(record.getLawyerId())) {
                    String[] split = record.getLawyerId().split(",");
                    List<String> names = new ArrayList<>();
                    for (String id : split) {
                        names.add(userMap.get(Integer.parseInt(id)));
                    }
                    record.setLawyerName(String.join("，", names));
                }
                if (Objects.nonNull(record.getBusinessType())) {
                    record.setBusinessName(dictMap.get(String.valueOf(record.getBusinessType())));
                }
                List<SysFile> contractFile = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizId, record.getId()).eq(SysFile::getBizType, 6).eq(SysFile::getIsDel, 0));
                record.setContractFile(contractFile);
                List<SysFile> invoiceFile = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizId, record.getId()).eq(SysFile::getBizType, 7).eq(SysFile::getIsDel, 0));
                record.setInvoiceFile(invoiceFile);
            }
        }
        return selectPage;
    }


    /**
     * 存草稿
     */
    public void save(NonLitigationBusiness entity) {
        SysUser loginUser = userService.getUser();
        List<SysRole> roleList = loginUser.getRoleList();
        List<String> roleCodes = CollUtil.isEmpty(roleList)
                ? Collections.emptyList()
                : roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        if (!roleCodes.contains("ls")) {
            throw new CustomException("抱歉，您没有操作权限");
        }
        Date now = new Date();
        entity.setStatus(0);
        entity.setIsDel(0);
        entity.setUpdateUser(loginUser.getId());
        entity.setUpdateTime(now);

        if (Objects.isNull(entity.getId())) {
            entity.setCreateUser(loginUser.getId());
            entity.setCreateTime(now);
            nonLitigationBusinessMapper.insert(entity);
        }else {
            nonLitigationBusinessMapper.updateById(entity);
        }

        List<SysFile> sysFiles = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizType, 6).eq(SysFile::getBizId, entity.getId()));
        for (SysFile file : sysFiles) {
            file.setIsDel(1);
            fileMapper.updateById(file);
        }
        if (CollUtil.isNotEmpty(entity.getContractFile())) {
            for (SysFile file : entity.getContractFile()) {
                file.setBizId(entity.getId());
                file.setIsDel(0);
                fileMapper.updateById(file);
            }
        }
    }

    /**
     * 获取草稿
     * @return
     */
    public NonLitigationBusiness getDraft() {
        SysUser loginUser = userService.getUser();
        NonLitigationBusiness business = nonLitigationBusinessMapper.selectOne(new LambdaQueryWrapper<NonLitigationBusiness>()
                .eq(NonLitigationBusiness::getIsDel, 0)
                .eq(NonLitigationBusiness::getStatus, 0)
                .eq(NonLitigationBusiness::getCreateUser, loginUser.getId())
        );
        List<SysFile> contractFile = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizId, business.getId()).eq(SysFile::getBizType, 6).eq(SysFile::getIsDel, 0));
        business.setContractFile(contractFile);
        List<SysFile> invoiceFile = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizId, business.getId()).eq(SysFile::getBizType, 7).eq(SysFile::getIsDel, 0));
        business.setInvoiceFile(invoiceFile);
        return business;
    }

    /**
     * 提交
     */
    public void submit(NonLitigationBusiness entity) {
        SysUser loginUser = userService.getUser();
        List<SysRole> roleList = loginUser.getRoleList();
        List<String> roleCodes = CollUtil.isEmpty(roleList)
                ? Collections.emptyList()
                : roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        if (!roleCodes.contains("ls")) {
            throw new CustomException("抱歉，您没有操作权限");
        }
        Date now = new Date();

        String today = DateUtil.format(now, "yyyyMMdd");
        Long count = nonLitigationBusinessMapper.selectCount(new LambdaQueryWrapper<NonLitigationBusiness>()
                .gt(NonLitigationBusiness::getCreateTime, DateUtil.beginOfDay(now))
        );
        String countStr = String.format("%03d", count);

        entity.setCode(today + countStr);
        entity.setStatus(1);
        entity.setIsDel(0);
        entity.setUpdateUser(loginUser.getId());
        entity.setUpdateTime(now);
        if (Objects.isNull(entity.getId())) {
            entity.setCreateTime(now);
            entity.setCreateUser(loginUser.getId());
            nonLitigationBusinessMapper.insert(entity);
        }else {
            nonLitigationBusinessMapper.updateById(entity);
        }
        List<SysFile> sysFiles = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getBizType, 6).eq(SysFile::getBizId, entity.getId()));
        for (SysFile file : sysFiles) {
            file.setIsDel(1);
            fileMapper.updateById(file);
        }
        if (CollUtil.isNotEmpty(entity.getContractFile())) {
            for (SysFile file : entity.getContractFile()) {
                file.setBizId(entity.getId());
                file.setIsDel(0);
                fileMapper.updateById(file);
            }
        }
    }

    public void check(NonLitigationBusiness entity) {
        NonLitigationBusiness select = nonLitigationBusinessMapper.selectById(entity.getId());
        if (!Objects.equals(1, select.getStatus()) || Objects.equals(3, select.getStatus())) {
            throw new CustomException("操作失败，请刷新页面后操作");
        }
        SysUser loginUser = userService.getUser();
        Date now = new Date();
        if (!Objects.equals("wenquan", loginUser.getNameSpelling())) {
            throw new CustomException("抱歉，您没有审批权限");
        }
        if (entity.getCheckResult() == 1) {
            entity.setStatus(2);
            entity.setCheckDesc("");
        }else {
            entity.setStatus(3);
        }
        entity.setCheckUser(loginUser.getId());
        entity.setCheckTime(now);
        nonLitigationBusinessMapper.updateById(entity);
    }


    public void delById(NonLitigationBusiness entity) {
        SysUser loginUser = userService.getUser();
        List<SysRole> roleList = loginUser.getRoleList();
        List<String> roleCodes = CollUtil.isEmpty(roleList)
                ? Collections.emptyList()
                : roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

        NonLitigationBusiness select = nonLitigationBusinessMapper.selectById(entity.getId());
        if (Objects.equals(select.getCreateUser(), loginUser.getId()) || roleCodes.contains("admin1")) {
            Date now = new Date();
            entity.setUpdateUser(loginUser.getId());
            entity.setUpdateTime(now);
            entity.setIsDel(1);
            nonLitigationBusinessMapper.updateById(entity);
        }else {
            throw new CustomException("抱歉，您没有操作权限");
        }

    }
}
