package com.jsdc.lsoa.common.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler)->{
            SaRouter.notMatch("/*.html")
                    .notMatch("/swagger-resources")
                    .notMatch("/webjars/**")
                    .notMatch("/**/api-docs")
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**").excludePathPatterns("/api/login", "/api/logout","/api/sysDict/insertOrEditDict","/api/sysDict/refreshReids");
    }

    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        // token名称 (同时也是cookie名称)
        config.setTokenName("accessToken");
        // token风格
        config.setTokenStyle("token-style=random-128");
        // 设置有效期30天（单位：秒）
        config.setTimeout(30  * 24 * 60 * 60);
        // token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒，支持自动续签
        config.setActivityTimeout(-1);
        // 自动续签，指定时间内有操作，则会自动续签
        config.setAutoRenew(true);
        // 是否尝试从header里读取token
        config.setIsReadHead(true);
        // 是否尝试从cookie里读取token
        config.setIsReadCookie(true);
        // 是否尝试从请求体里读取token
        config.setIsReadBody(false);
        // 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
        config.setIsConcurrent(false);
        // 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
        config.setIsShare(false);
        // 是否在初始化配置时打印版本字符画
        config.setIsPrint(false);
        // 是否输出操作日志
        config.setIsLog(false);
        return config;
    }
}
