package com.jsdc.lsoa.controller;


import com.jsdc.lsoa.model.AgreementCustomer;
import com.jsdc.lsoa.service.AgreementCustomerService;
import com.jsdc.lsoa.vo.AgreementCustomerVO;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreement/customer")
@Api(tags = "协议客户")
public class AgreementCustomerController {


    @Autowired
    private AgreementCustomerService agreementCustomerService;


    @PostMapping("/page")
    @ApiOperation("分页查询")
    public ResultInfo getPageList(@RequestBody AgreementCustomerVO vo) {
        return ResultInfo.success(agreementCustomerService.getPageList(vo));
    }


    @PostMapping("/save")
    @ApiOperation("新增或编辑")
    public ResultInfo save(@RequestBody AgreementCustomerVO entity) {
        agreementCustomerService.saveOrUp(entity);
        return ResultInfo.success();
    }


    @PostMapping("/delById")
    @ApiOperation("删除")
    public ResultInfo delById(@RequestBody AgreementCustomer entity) {
        agreementCustomerService.delById(entity);
        return ResultInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("列表查询")
    public ResultInfo getPageList() {
        return ResultInfo.success(agreementCustomerService.list());
    }

    @PostMapping("/detail")
    @ApiOperation("详情")
    public ResultInfo detail(@RequestBody AgreementCustomer entity) {
        return ResultInfo.success(agreementCustomerService.detail(entity.getId()));
    }

    @PostMapping("/saveOrUpdateInvoiceInfo")
    @ApiOperation("开票")
    public ResultInfo saveOrUpdateInvoiceInfo(@RequestBody InvoiceInfoVo entity) {
        return ResultInfo.success(agreementCustomerService.saveOrUpdateInvoiceInfo(entity));
    }

    @PostMapping("/invoiceInfo/detail")
    @ApiOperation("开票详情")
    public ResultInfo invoiceInfoDetail(@RequestBody AgreementCustomer entity) {
        return agreementCustomerService.invoiceInfoDetail(entity.getId());
    }

    @GetMapping("/list/byRole")
    @ApiOperation("根据角色查询顾问单位列表")
    public ResultInfo getListByRole() {
        return ResultInfo.success(agreementCustomerService.getListByRole());
    }

}
