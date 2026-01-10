package com.jsdc.lsoa.log;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.jsdc.lsoa.model.SysLog;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.service.SysLogService;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class LogAspect {
    @Autowired
    private SysLogService logService;

    ThreadLocal<SysUser> userThreadLocal = new ThreadLocal<>();

    @Pointcut(value = "@annotation(com.jsdc.lsoa.log.LogInfo)")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void doBeforeReturning(JoinPoint joinPoint) {
        try {
            // 当前登录用户
            SysUser user = (SysUser) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
            userThreadLocal.set(user);
        }catch (Exception ignored) {}
    }


    @AfterReturning(value = "logPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            SysUser sysUser = userThreadLocal.get();
            if (sysUser == null) {
                try {
                    sysUser = (SysUser) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
                }catch (NotLoginException notLoginException) {
                    return;
                }
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            // 请求接口地址
            String api = request.getRequestURI();
            // 客户端IP地址
            String ip = IPUtil.getIpAddr(request);

            String requestMethod = request.getMethod();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogInfo logInfo = method.getAnnotation(LogInfo.class);

            Object[] args = joinPoint.getArgs();
            String requestBody = Arrays.stream(args)
                    .map(arg -> {
                        if (arg instanceof MultipartFile) {
                            MultipartFile file = (MultipartFile) arg;
                            // 记录文件名和大小，避免转换文件流
                            return "MultipartFile[name=" + file.getOriginalFilename() + ", size=" + file.getSize() + "]";
                        }else if (arg instanceof HttpServletResponse) {
                            // 跳过 HttpServletResponse 的序列化
                            return "HttpServletResponse[skipped]";
                        } else {
                            try {
                                // 对于其他参数，尝试转换为 JSON 字符串
                                return JSON.toJSONString(arg);
                            } catch (Exception e) {
                                // 若转换失败，记录异常并跳过
                                return "请求内容无法序列化";
                            }
                        }
                    }).collect(Collectors.joining(", "));
            // 响应参数
            String responseBody;
            // 检查返回值是否是流文件类型
            if (result instanceof InputStream || result instanceof OutputStream || result instanceof Writer) {
                // 如果是流文件，直接返回，不进行日志记录转换
                responseBody = "响应内容为流文件，无法序列化";
            } else {
                try {
                    // 非流文件，记录响应内容
                    responseBody = JSON.toJSONString(result);
                } catch (Exception e) {
                    responseBody = "响应内容无法序列化";
                }

            }
            logService.save( logInfo.mode(), logInfo.type(), requestMethod, ip, api, 0, requestBody, responseBody, sysUser.getId(), sysUser.getRealName());
        }catch (Exception e) {
            log.error("操作日志保存失败：", e);
        }
    }


    @AfterThrowing(value = "logPointcut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        try {
            SysUser sysUser = userThreadLocal.get();
            if (sysUser == null) {
                try {
                    sysUser = (SysUser) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
                }catch (NotLoginException notLoginException) {
                    return;
                }
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            // 请求接口地址
            String api = request.getRequestURI();
            // 客户端IP地址
            String ip = IPUtil.getIpAddr(request);

            String requestMethod = request.getMethod();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogInfo logInfo = method.getAnnotation(LogInfo.class);

            Object[] args = joinPoint.getArgs();
            String requestBody = Arrays.stream(args)
                    .map(arg -> {
                        if (arg instanceof MultipartFile) {
                            MultipartFile file = (MultipartFile) arg;
                            // 记录文件名和大小，避免转换文件流
                            return "MultipartFile[name=" + file.getOriginalFilename() + ", size=" + file.getSize() + "]";
                        }else if (arg instanceof HttpServletResponse) {
                            // 跳过 HttpServletResponse 的序列化
                            return "HttpServletResponse[skipped]";
                        } else {
                            try {
                                // 对于其他参数，尝试转换为 JSON 字符串
                                return JSON.toJSONString(arg);
                            } catch (Exception e) {
                                // 若转换失败，记录异常并跳过
                                return "请求内容无法序列化";
                            }
                        }
                    }).collect(Collectors.joining(", "));
            // 响应参数
            String responseBody = ex.getMessage();
            logService.save(logInfo.mode(), logInfo.type(), requestMethod, ip, api, 1, requestBody, responseBody, sysUser.getId(), sysUser.getRealName());
        }catch (Exception e) {
            log.error("操作日志保存失败：", e);
        }
    }

}
