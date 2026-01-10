package com.jsdc.lsoa.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.init.RedisDataInit;
import com.jsdc.lsoa.mapper.SysDictMapper;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sys/config")
@Api(tags = "系统配置")
public class SysConfigController {

    @Autowired
    private SysDictMapper dictMapper;

    @Autowired
    private RedisDataInit redisDataInit;

    @GetMapping("/fileType")
    @ApiOperation("当前附件类型")
    public ResultInfo<List<String>> fileType() {
        List<SysDict> list = dictMapper.selectList(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)
                .eq(SysDict::getDictType, CommonConstants.FILE_TYPE_KEY)
                .eq(SysDict::getMemo, "CASE")
        );
        List<String> strings = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            strings = list.stream().map(SysDict::getDictLabel).collect(Collectors.toList());
        }
        return ResultInfo.success(strings);
    }

    @PostMapping("/fileType/add")
    @ApiOperation("新增附件类型")
    public ResultInfo<Void> addFileType(@RequestBody String value) {
        List<SysDict> list = dictMapper.selectList(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)
                .eq(SysDict::getDictType, CommonConstants.FILE_TYPE_KEY)
                .eq(SysDict::getMemo, "CASE")
        );
        SysDict selected = dictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)
                .eq(SysDict::getDictType, CommonConstants.FILE_TYPE_KEY)
                .eq(SysDict::getDictLabel, value)
                .eq(SysDict::getMemo, "CASE")
        );
        if (Objects.nonNull(selected)) {
            throw new CustomException("文件类型已存在");
        }
        SysDict entity = new SysDict();
        if (CollUtil.isEmpty(list)) {
            entity.setDictValue("1");
            entity.setSort("1");
        }else {
            SysDict dict = list.stream().max(Comparator.comparing(o -> Integer.parseInt(o.getDictValue()))).orElse(null);
            int val = 0;
            if (Objects.nonNull(dict)) {
                val = Integer.parseInt(dict.getDictValue());
            }
            entity.setDictValue(String.valueOf(val + 1));
            entity.setSort(String.valueOf(val + 1));
        }
        entity.setDictType(CommonConstants.FILE_TYPE_KEY);
        entity.setDictTypeName("案件文件类型");
        entity.setDictLabel(value);
        entity.setMemo("CASE");
        entity.setIsDel(0);
        dictMapper.insert(entity);
        redisDataInit.init();
        return ResultInfo.success();
    }

    @PostMapping("/fileType/del")
    @ApiOperation("删除附件类型")
    public ResultInfo<Void> delFileType(@RequestBody String value) {
        SysDict dict = dictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)
                .eq(SysDict::getDictType, CommonConstants.FILE_TYPE_KEY)
                .eq(SysDict::getMemo, "CASE")
                .eq(SysDict::getDictLabel, value)
        );
        if (Objects.nonNull(dict)) {
            dictMapper.deleteById(dict.getId());
            redisDataInit.init();
        }
        return ResultInfo.success();
    }


    @GetMapping("/dict/list")
    @ApiOperation("地址设置和空间配置")
    public ResultInfo<Map<String, SysDict>> getDictList() {
        List<String> typeList = Arrays.asList(CommonConstants.FILE_DISK_PATH_KEY, CommonConstants.FILE_DOMAIN_KEY, CommonConstants.FILE_TYPE_CONFIG_KEY, CommonConstants.FILE_MAX_SIZE_KEY, CommonConstants.FILE_TYPE_CONFIG_KEY);
        List<SysDict> list = dictMapper.selectList(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getIsDel, 0)
                .in(SysDict::getDictType, typeList)
        );
        Map<String, SysDict> collect = list.stream().collect(Collectors.toMap(SysDict::getDictType, o -> o));
        return ResultInfo.success(collect);
    }


    @PostMapping("/dict/save")
    @ApiOperation("修改地址设置和空间配置")
    public ResultInfo<Void> saveDict(@RequestBody List<SysDict> dictList) {
        for (SysDict dict : dictList) {
            SysDict entity = new SysDict();
            entity.setId(dict.getId());
            entity.setDictValue(dict.getDictValue());
            dictMapper.updateById(entity);
        }
        redisDataInit.init();
        return ResultInfo.success();
    }


}
