package com.jsdc.lsoa.service;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.SysFileMapper;
import com.jsdc.lsoa.model.SysFile;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class SysFileService extends ServiceImpl<SysFileMapper, SysFile> {

    @Autowired
    private SysFileMapper fileMapper;

    @Autowired
    private SysUserService userService;

    public SysFile uploadFile(MultipartFile file, Integer bizType, Integer bizId) {
        SysUser loginUser = userService.getUser();
        Date now = new Date();
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
        // 文件类型限制，英文逗号分隔
        Map<String, String> fileTypeMap = (Map<String, String>) RedisUtils.getBeanValue(CommonConstants.FILE_TYPE_CONFIG_KEY);
        String fileType = fileTypeMap.keySet().iterator().next();
        if (StringUtils.isNotBlank(fileType)) {
            List<String> fileTypeList = Arrays.asList(fileType.split(","));
            if (!fileTypeList.contains(suffix)) {
                throw new CustomException("不允许上传此格式文件");
            }
        }

        // 文件大小限制 单位 M
        Map<String, String> maxSizeMap = (Map<String, String>) RedisUtils.getBeanValue(CommonConstants.FILE_MAX_SIZE_KEY);
        String maxSize = maxSizeMap.keySet().iterator().next();

        BigDecimal base = new BigDecimal("1024");
        BigDecimal size = BigDecimal.valueOf(file.getSize());
        BigDecimal max = new BigDecimal(maxSize).multiply(base).multiply(base);
        if (size.compareTo(max) > 0) {
            throw new CustomException("文件大小超出上传限制");
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

        SysFile entity = new SysFile();
        entity.setBizType(bizType);
        entity.setBizId(bizId);
        entity.setOriginalFilename(originalFilename);
        entity.setFilename(filename);
        entity.setFilePath(filePath);
        entity.setFileType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setFileUrl(fileUrl);
        entity.setIsDel(0);
        entity.setCreateUser(loginUser.getId());
        entity.setUpdateUser(loginUser.getId());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        fileMapper.insert(entity);

        return entity;
    }

    public void deleteById(Integer id) {
        SysFile file = fileMapper.selectById(id);
        file.setIsDel(1);
        fileMapper.updateById(file);
    }

    public List<SysFile> list(SysFile entity) {
        return fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getIsDel, CommonConstants.ISDEL_NO)
                .eq(Objects.nonNull(entity.getBizType()), SysFile::getBizType, entity.getBizType())
                .eq(Objects.nonNull(entity.getBizId()), SysFile::getBizId, entity.getBizId())
        );
    }
}
