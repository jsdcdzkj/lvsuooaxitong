package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.SysLogService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  日志管理
 */
@RestController
@RequestMapping("/api/sysLog")
@Api(tags = "日志管理")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页查询
     */
    @PostMapping("/getPageList")
    public ResultInfo getPageList(@RequestBody SysLogVo bean) {
        return ResultInfo.success(sysLogService.page(bean));
    }
}
