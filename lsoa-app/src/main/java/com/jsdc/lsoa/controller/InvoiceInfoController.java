package com.jsdc.lsoa.controller;

import cn.hutool.core.collection.CollUtil;
import com.jsdc.lsoa.model.ClientInfo;
import com.jsdc.lsoa.service.ClientInfoService;
import com.jsdc.lsoa.service.InvoiceInfoService;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.InvoiceInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 *  todo  
 *  controller控制器
 */
@RestController
@RequestMapping("/app/invoiceInfo")
@Api(tags = "开票")
public class InvoiceInfoController {

    @Autowired
    private InvoiceInfoService invoiceInfoService;

    @Autowired
    private ClientInfoService clientInfoService;

    /**
     * 分页查询 todo
     *
     * @return
     */
    @PostMapping("/getPageList")
    @ApiOperation("查询")
    public ResultInfo getPageList(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(invoiceInfoService.getPageList(bean));
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("开票")
    public ResultInfo saveOrUpdateInvoiceInfo(@RequestBody InvoiceInfoVo bean) {
        return invoiceInfoService.saveOrUpdateInvoiceInfo(bean);
    }

    /**
     * 获取实体类
     * @param bean
     * @return
     */
    @PostMapping("/getEntity")
    @ApiOperation("详情")
    public ResultInfo getEntity(@RequestBody InvoiceInfoVo bean) {
        return invoiceInfoService.getEntityById(bean.getId());
    }

    @PostMapping("/approveInvoiceInfo")
    @ApiOperation("审核")
    public ResultInfo  approveInvoiceInfo(@RequestBody InvoiceInfoVo bean){
        return invoiceInfoService.approveInvoiceInfo(bean);
    }


    @PostMapping("/toInvoice")
    @ApiOperation("根据ID查询")
    public ResultInfo toInvoice(@RequestBody InvoiceInfoVo bean) {
        return ResultInfo.success(invoiceInfoService.toInvoice(bean.getId()));
    }
    @PostMapping("/getClient")
    @ApiOperation("查询当事人")
    public ResultInfo  getClient(@RequestBody ClientInfo bean){
        HashMap<String, ClientInfo> map = new HashMap<>();
        map.put("person",null);
        map.put("corporation",null);
        map.put("households",null);
        map.put("victim",null);
        map.put("customer",null);
        bean.setPlainOrDefend("1");
        bean.setIsPerson("1");
        List<ClientInfo> person = clientInfoService.getList(bean);
        if (CollUtil.isNotEmpty(person)) {
            map.put("person",person.get(0));
        }
        bean.setIsPerson("2");
        List<ClientInfo> corporation = clientInfoService.getList(bean);
        if (CollUtil.isNotEmpty(corporation)) {
            map.put("corporation",corporation.get(0));
        }
        bean.setIsPerson("3");
        List<ClientInfo> households = clientInfoService.getList(bean);
        if (CollUtil.isNotEmpty(households)) {
            map.put("households",households.get(0));
        }
        bean.setIsPerson("4");
        List<ClientInfo> victim = clientInfoService.getList(bean);
        if (CollUtil.isNotEmpty(victim)) {
            map.put("victim",victim.get(0));
        } bean.setIsPerson("5");
        List<ClientInfo> customer = clientInfoService.getList(bean);
        if (CollUtil.isNotEmpty(customer)) {
            map.put("customer",customer.get(0));
        }

        return ResultInfo.success(map);
    }

    @PostMapping("/getList")
    @ApiOperation("根据ID查询")
    public ResultInfo getList(@RequestBody CaseInfoVo bean) {
        return ResultInfo.success(invoiceInfoService.getList(bean));
    }

}
