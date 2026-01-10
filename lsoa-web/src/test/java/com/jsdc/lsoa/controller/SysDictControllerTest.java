package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.service.SysDictService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDictVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class SysDictControllerTest {
    @Autowired
    private SysDictService dictService;
    @Autowired
    private SysDictController sysDictController;

    @Test
    void getCaseFileType() {
        ResultInfo entity = sysDictController.getCaseFileType();
        System.out.println(entity);
    }



    @Test
    void getPageList() {
        SysDictVo bean = new SysDictVo();
        bean.setDictLabel("刑事");
        ResultInfo pageList = sysDictController.getPageList(bean);
        System.out.println(pageList);
    }

    @Test
    void saveOrUpdateSysDict() {
        SysDictVo bean = new SysDictVo();
        bean.setDictValue("1");
        bean.setDictType("caseType");
        bean.setDictTypeName("案件类型");
        bean.setDictLabel("刑事案件");
        bean.setMemo("");
        dictService.saveOrUpdateSysDict(bean);
    }

    @Test
    void getEntity() {
        SysDictVo bean = new SysDictVo();
        bean.setId(1);
        ResultInfo entity = sysDictController.getEntity(bean);
        System.out.println(entity);
    }


    @Test
    void getRedisDictList() {
        SysDictVo bean = new SysDictVo();
        bean.setDictType("caseType");
        ResultInfo redisDictList = sysDictController.getRedisDictList(bean);
        System.out.println();
    }
}