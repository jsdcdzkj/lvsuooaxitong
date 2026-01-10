package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.CustomerLawyersService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.CustomerLawyersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  todo  
 *  controller控制器
 */
@RestController
@RequestMapping("/customerLawyers")
@Api(tags = "XXXXXXXXX")
public class CustomerLawyersController {

    @Autowired
    private CustomerLawyersService customerLawyersService;

    /**
     * 分页查询 todo
     *
     * @return
     */
    @PostMapping("/getPageList")
    //@ApiOperation("XXXX")
    public ResultInfo getPageList(@RequestBody CustomerLawyersVo bean) {
        return ResultInfo.success(customerLawyersService.getPageList(bean));
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    @PostMapping("/saveOrUpdate")
    //@ApiOperation("XXXX")
    public ResultInfo saveOrUpdateCustomerLawyers(@RequestBody CustomerLawyersVo bean) {
        return customerLawyersService.saveOrUpdateCustomerLawyers(bean);
    }

    /**
     * 获取实体类
     * @param bean
     * @return
     */
    @PostMapping("/getEntity")
    //@ApiOperation("XXXX")
    public ResultInfo getEntity(CustomerLawyersVo bean) {
        return customerLawyersService.getEntityById(bean.getId());
    }
}
