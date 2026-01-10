package com.jsdc.lsoa.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.init.RedisDataInit;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.service.SysDictService;
import com.jsdc.lsoa.utils.RedisUtils;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDictVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :wr
 */

@RestController
@RequestMapping("/api/sysDict")
@Api(tags = "字典")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private RedisDataInit redisDataInit;


    @PostMapping("/getRedisDictList")
    @ApiOperation("数据字典列表")
    public ResultInfo getRedisDictList(@RequestBody SysDict bean) {
        Map<String, List<SysDict>> dictLists = (Map<String, List<SysDict>>) RedisUtils.getBeanValue(CommonConstants.allDictListPrefix);
        List<SysDict> sdList = dictLists.get(bean.getDictType());
        return ResultInfo.success(sdList);
    }


    @PostMapping("/getCaseFileType")
    @ApiOperation("案件文件类型字典")
    public ResultInfo getCaseFileType() {
        Map<String, List<SysDict>> dictLists = (Map<String, List<SysDict>>) RedisUtils.getBeanValue(CommonConstants.allDictListPrefix);
        List<SysDict> sdList = dictLists.get("fileType");
        List<SysDict> aCase = sdList.stream().filter(x -> x.getMemo().equals("CASE") && x.getIsDel().equals(CommonConstants.ISDEL_NO)).collect(Collectors.toList());
        return ResultInfo.success(aCase);
    }

    /**
     * 分页查询 todo
     */
    @PostMapping("/getPageList")
    @ApiOperation("列表分页")
    public ResultInfo getPageList(@RequestBody SysDictVo bean) {
        return ResultInfo.success(sysDictService.getPageList(bean));
    }

    /**
     * 添加/编辑 todo
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存编辑")
    public ResultInfo saveOrUpdateSysDict(@RequestBody SysDictVo bean) {
        return sysDictService.saveOrUpdateSysDict(bean);
    }

    /**
     * 获取实体类
     */
    @PostMapping("/getEntity")
    @ApiOperation("获取实体类")
    public ResultInfo getEntity(@RequestBody SysDictVo bean) {
        return sysDictService.getEntityById(bean.getId());
    }

    /**
     * 删除数据
     *
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResultInfo delete(@RequestBody SysDictVo bean) {
        if (null == bean.getId()) {
            return ResultInfo.error("id不能为空");
        }
        LambdaUpdateWrapper<SysDict> wrapper = Wrappers.<SysDict>lambdaUpdate();
        wrapper.eq(SysDict::getId,  bean.getId());
        wrapper.set(SysDict::getIsDel, CommonConstants.ISDEL_YES);
        sysDictService.update(null, wrapper);
        SysDict sysDict = sysDictService.getById( bean.getId());
//        redisDataInit.updateDictCache(sysDict);
        return ResultInfo.success();
    }


    /**
     * 添加/编辑 todo
     */
    @GetMapping("/insertOrEditDict")
    @ApiOperation("url新增/编辑")
    public ResultInfo saveOrUpdateSysDict(String label,String type, String value ,String typeName) {
        SysDictVo bean = new SysDictVo();
        bean.setDictLabel(label);
        bean.setDictType(type);
        bean.setDictValue(value);
        bean.setDictTypeName(typeName);
        return sysDictService.saveOrUpdateSysDict(bean);
    }
    /**
     * 刷新redis
     */
    @GetMapping("/refreshReids")
    @ApiOperation("刷新redis")
    public ResultInfo refreshReids() {
        redisDataInit.init();
        return ResultInfo.success();
    }
}
