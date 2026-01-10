package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.service.SysMenuService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
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
public class SysMenuControllerTest {

    @Autowired
    private SysMenuService service;

    @Test
    public void getPage(){
        SysMenuVo vo=new SysMenuVo();
          ResultInfo.success(service.getPage(vo));
    }

    @Test
    public void saveOrUpdate(){
//        service.saveOrUpdateMenu(vo);
          ResultInfo.success();
    }

    @Test
    public void del(){
        SysMenuVo vo=new SysMenuVo();
        vo.setId(1);
          service.del(vo);
    }

    @Test
    public void getSysMenu(){
        SysMenuVo vo=new SysMenuVo();
          ResultInfo.success(service.getSysMenu(vo));
    }


    @Test
    public void getSysMenuTree(){
        SysMenuVo vo=new SysMenuVo();
          ResultInfo.success(service.getSysMenuTree(vo));
    }

    @Test
    public void getSysMenuList(){
        SysMenuVo vo=new SysMenuVo();
          ResultInfo.success(service.getSysMenuList(vo));
    }



}
