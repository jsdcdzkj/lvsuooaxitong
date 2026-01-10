package com.jsdc.lsoa.controller;


import com.jsdc.lsoa.model.NonLitigationBusiness;
import com.jsdc.lsoa.service.NonLitigationBusinessService;
import com.jsdc.lsoa.vo.NonLitigationBusinessVO;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/non/litigation/business")
@Api(tags = "非诉讼业务")
@RestController
public class NonLitigationBusinessController {

    @Autowired
    private NonLitigationBusinessService nonLitigationBusinessService;

    @PostMapping("/page")
    @ApiOperation("分页查询")
    public ResultInfo getPageList(@RequestBody NonLitigationBusinessVO vo) {
        return ResultInfo.success(nonLitigationBusinessService.getPageList(vo));
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    public ResultInfo save(@RequestBody NonLitigationBusiness entity) {
        nonLitigationBusinessService.save(entity);
        return ResultInfo.success();
    }

    @PostMapping("/getDraft")
    @ApiOperation("获取草稿")
    public ResultInfo getDraft() {
        return ResultInfo.success(nonLitigationBusinessService.getDraft());
    }

    @PostMapping("/submit")
    @ApiOperation("提交")
    public ResultInfo submit(@RequestBody NonLitigationBusiness entity) {
        nonLitigationBusinessService.submit(entity);
        return ResultInfo.success();
    }

    @PostMapping("/check")
    @ApiOperation("审批")
    public ResultInfo check(@RequestBody NonLitigationBusiness entity) {
        nonLitigationBusinessService.check(entity);
        return ResultInfo.success();
    }

    @PostMapping("/delById")
    @ApiOperation("删除")
    public ResultInfo delById(@RequestBody NonLitigationBusiness entity) {
        nonLitigationBusinessService.delById(entity);
        return ResultInfo.success();
    }


}
