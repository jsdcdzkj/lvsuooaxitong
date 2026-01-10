package com.jsdc.lsoa.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.SysUserMapper;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.utils.MD5Utils;
import com.jsdc.lsoa.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
public class MainService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    public SaTokenInfo webLogin(LoginVO vo) {
        if (StringUtils.isAnyBlank(vo.getAccount(), vo.getPassword())) {
            throw new CustomException("账号或密码不能为空");
        }
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsDel, 0).and(
                        wrapper -> wrapper.eq(SysUser::getRealName, vo.getAccount())
                                .or()
                                .eq(SysUser::getNameSpelling, vo.getAccount())
                                .or()
                                .eq(SysUser::getPhone, vo.getAccount())
                )
        );
        if (Objects.isNull(sysUser)) {
            throw new CustomException("账号不存在！");
        }
        if (sysUser.getStatus() == null || sysUser.getStatus() == 1) {
            throw new CustomException("账号已被禁用！");
        }
        String password = MD5Utils.getMD5(vo.getPassword());
        if (!Objects.equals(password, sysUser.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        StpUtil.login(sysUser.getId(), applicationName);
        StpUtil.getSession().set(sysUser.getId().toString(), sysUser);
        return StpUtil.getTokenInfo();
    }

    public void webLogout() {
        if (StpUtil.isLogin()) {
            Object id = StpUtil.getLoginId();
            StpUtil.logout(id, applicationName);
        }
    }
}
