package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.model.ProcessNode;
import com.jsdc.lsoa.service.ProcessNodeService;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/process/node")
@Api(tags = "流程节点")
@RestController
public class ProcessNodeController {


    @Autowired
    private ProcessNodeService processNodeService;


    @GetMapping("/list")
    @ApiOperation("根据流程ID查询列表")
    public ResultInfo<List<ProcessNode>> getByProcess(@RequestParam Integer processId) {
        List<ProcessNode> list = processNodeService.getByProcess(processId);
        return ResultInfo.success(list);
    }


    @PostMapping("/save")
    @ApiOperation("新增或编辑")
    public ResultInfo<Void> save(@RequestBody ProcessNode entity) {
        processNodeService.save(entity);
        return ResultInfo.success();
    }


    @PostMapping("/del")
    @ApiOperation("根据ID删除节点")
    public ResultInfo<Void> deleteById(@RequestBody ProcessNode entity) {
        processNodeService.deleteById(entity.getId());
        return ResultInfo.success();
    }

}
