package com.jsdc.lsoa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.mapper.SysDictMapper;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/sys/config")
@Api(tags = "系统配置")
public class SysConfigController {

    @Autowired
    private SysDictMapper dictMapper;

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

}
