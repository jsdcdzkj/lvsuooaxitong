package com.jsdc.lsoa.controller;


import com.jsdc.lsoa.service.AgreementCustomerService;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/agreement/customer")
@Api(tags = "协议客户")
public class AgreementCustomerController {

    @Autowired
    private AgreementCustomerService agreementCustomerService;

    @GetMapping("/list")
    @ApiOperation("列表查询")
    public ResultInfo getPageList() {
        return ResultInfo.success(agreementCustomerService.list());
    }

    @GetMapping("/list/byRole")
    @ApiOperation("根据角色查询顾问单位列表")
    public ResultInfo getListByRole() {
        return ResultInfo.success(agreementCustomerService.getListByRole());
    }
}
