package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.model.ProcessDesign;
import com.jsdc.lsoa.service.ProcessDesignService;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/process/design")
@Api(tags = "流程设计")
@RestController
public class ProcessDesignController {

    @Autowired
    private ProcessDesignService processDesignService;

    @PostMapping("/list")
    @ApiOperation("列表")
    public ResultInfo<List<ProcessDesign>> list() {
        List<ProcessDesign> list = processDesignService.list();
        return ResultInfo.success(list);
    }

    @GetMapping("/entity")
    @ApiOperation("根据ID查询")
    public ResultInfo<ProcessDesign> entity(@RequestParam Integer id) {
        ProcessDesign entity = processDesignService.getById(id);
        return ResultInfo.success(entity);
    }

    @PostMapping("/save")
    @ApiOperation("新增或编辑")
    public ResultInfo<Void> save(@RequestBody ProcessDesign entity) {
        processDesignService.save(entity);
        return ResultInfo.success();
    }


}
