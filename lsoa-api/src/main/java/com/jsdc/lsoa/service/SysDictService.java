package com.jsdc.lsoa.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.dao.SysDictDao;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.init.RedisDataInit;
import com.jsdc.lsoa.mapper.SysDictMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SysDictService extends BaseService<SysDict>  {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 分页查询 todo
     *
     * @return
     */
    public Page<SysDict> getPageList(SysDictVo vo) {
        Page<SysDict> sysDictPage = sysDictMapper.selectPage(
                new Page<>(vo.getPageNo(), vo.getPageSize()),
                Wrappers.<SysDict>lambdaQuery()
                        .eq(StrUtil.isNotBlank(vo.getDictType()),SysDict::getDictType, vo.getDictType())
                        .like(StrUtil.isNotBlank(vo.getDictLabel()),SysDict::getDictLabel, vo.getDictLabel())
        );
        return sysDictPage;

    }

    /**
     * 查询 todo
     *
     * @return
     */
    public List<SysDict> getList(SysDict entity) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        return sysDictMapper.selectList(queryWrapper);
    }

    /**
     * 添加/编辑 todo
     */
    public ResultInfo saveOrUpdateSysDict(SysDict sysDict) {
        // 校验
        list(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictValue, sysDict.getDictValue())
                .eq(SysDict::getDictType, sysDict.getDictType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)).stream().findFirst().ifPresent(dict -> {
            if (null == sysDict.getId() || !sysDict.getId().equals(dict.getId())) {
                throw new CustomException("字典值已存在");
            }
        });
        list(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictLabel, sysDict.getDictLabel())
                .eq(SysDict::getDictType, sysDict.getDictType()).eq(SysDict::getIsDel, CommonConstants.ISDEL_NO)).stream().findFirst().ifPresent(dict -> {
            if (null == sysDict.getId() || !sysDict.getId().equals(dict.getId())) {
                throw new CustomException("字典标签已存在");
            }
        });
        // 数据组装
        sysDict.setIsDel(CommonConstants.ISDEL_NO);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (null != sysDict && null != sysDict.getId() && null != sysDict.selectById(sysDict.getId())){
            sysDict.setUpdateTime(format);
            sysDict.setUpdateUser(sysUserService.getUser().getId().toString());
        }else {
            sysDict.setCreateTime(format);
            sysDict.setCreateUser(sysUserService.getUser().getId().toString());
        }
        saveOrUpdate(sysDict);
        return ResultInfo.success();
    }

    /**
     * 根据id获取类 todo
     *
     * @param id
     * @return
     */
    public ResultInfo getEntityById(Integer id) {
        return ResultInfo.success(getEntityById(id));
    }

    /**
     * @description 查询所有字典数据
     */
    public List<SysDict> getAllDicts() {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getIsDel, "0");
        return list(wrapper);
    }


    public List<SysDict> getListByType(String type) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dictType", type);
        queryWrapper.eq("isDel", "0");
        return sysDictMapper.selectList(queryWrapper);
    }
}


