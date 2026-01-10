package com.jsdc.lsoa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.mapper.SysLogMapper;
import com.jsdc.lsoa.model.SysLog;
import com.jsdc.lsoa.vo.SysLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class SysLogService extends BaseService<SysLog>  {

    @Autowired
    private SysLogMapper logMapper;

    public Page<SysLog> page(SysLogVo vo) {
        Page<SysLog> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        Page<SysLog> result = logMapper.selectPage(page, new LambdaQueryWrapper<SysLog>()
                .eq(StringUtils.isNotBlank(vo.getType()), SysLog::getType, vo.getType())
                .eq(StringUtils.isNotBlank(vo.getMethod()), SysLog::getMethod, vo.getMethod())
                .eq(StringUtils.isNotBlank(vo.getApi()), SysLog::getApi, vo.getApi())
                .eq(Objects.nonNull(vo.getStatus()), SysLog::getStatus, vo.getStatus())
                .like(StringUtils.isNotBlank(vo.getModeName()), SysLog::getModeName, vo.getModeName())
                .like(StringUtils.isNotBlank(vo.getOperatorName()), SysLog::getOperatorName, vo.getOperatorName())
                .between(Objects.nonNull(vo.getStartTime()) && Objects.nonNull(vo.getEndTime()), SysLog::getOperateTime, vo.getStartTime(), vo.getEndTime())
                .orderByDesc(SysLog::getId)
        );
        return result;
    }

    public void save(String modeName, String type, String method, String ip, String api, Integer status, String requestBody, String responseBody, Integer userId, String username) {
        SysLog entity = new SysLog();
        entity.setModeName(modeName);
        entity.setType(type);
        entity.setMethod(method);
        entity.setRequestIp(ip);
        entity.setApi(api);
        entity.setStatus(status);
        entity.setRequestBody(requestBody);
        entity.setResponseBody(responseBody);
        entity.setOperator(userId);
        entity.setOperatorName(username);
        entity.setOperateTime(new Date());
        logMapper.insert(entity);
    }

    public void save(String modeName, String type, String method, String ip, String api, Integer status, String requestBody, String responseBody) {
        SysLog entity = new SysLog();
        entity.setModeName(modeName);
        entity.setType(type);
        entity.setMethod(method);
        entity.setRequestIp(ip);
        entity.setApi(api);
        entity.setStatus(status);
        entity.setRequestBody(requestBody);
        entity.setResponseBody(responseBody);
        entity.setOperatorName("系统触发");
        entity.setOperateTime(new Date());
        logMapper.insert(entity);
    }
}


