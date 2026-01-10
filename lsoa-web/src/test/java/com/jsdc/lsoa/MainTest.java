package com.jsdc.lsoa;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.vo.SysUserSaveVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MainTest {


    @Autowired
    private SysUserService userService;


    @Test
    public void test1() {
        SysUserSaveVO vo = new SysUserSaveVO();
        vo.setRealName("admin");
        vo.setPhone("12345678901");
        userService.save(vo);
    }

    public static void main(String[] args) {

        // "摩羯座"
        String zodiac = DateUtil.getZodiac(Month.AUGUST.getValue(), 18);

        // "狗"
        String chineseZodiac = DateUtil.getChineseZodiac(2001);
        System.out.println(zodiac);
        System.out.println(chineseZodiac);

    }
}
