package com.jsdc.lsoa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysDeptService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDeptVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ：苹果
 * @Description：
 * @Date ：2024/12/19 16:10
 * @Modified By：
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SysDeptControllerTest {

    @Autowired
    private SysDeptService service;


    @Test
    public void getPage(){
        SysDeptVo vo=new SysDeptVo();
        Page page = service.getPage(vo);
    }

    @Test
    public void saveOrUpdate(){
        SysDeptVo vo=new SysDeptVo();
        vo.setName("案件列表");
          service.saveOrUpdateDept(vo);
    }

    @Test
    public void del(){
        SysDeptVo vo=new SysDeptVo();
        vo.setId(1);
//          service.del(vo);
    }


    @Test
    public void getSysDept(){
        SysDeptVo vo=new SysDeptVo();
        vo.setId(1);
          service.getSysDept(vo);
    }


    @Test
    public void getSysDeptList(){
        SysDeptVo vo=new SysDeptVo();
        vo.setName("案件列表");
        service.getSysDeptList(vo);
    }

}
