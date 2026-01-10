package com.jsdc.lsoa.init;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.lsoa.constants.CommonConstants;
import com.jsdc.lsoa.mapper.SysUserMapper;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.service.SysDictService;
import com.jsdc.lsoa.service.SysUserService;
import com.jsdc.lsoa.utils.RedisUtils;
import com.jsdc.lsoa.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisDataInit {
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserService userService;
    /**
     * 初始化数据
     */
    public void init() {
        dictInit();
        usersInit();
        putAllDict();
    }
    /**
     * @description 初始化字典数据，缓存redis
     */
    public void dictInit() {
        List<SysDict> list = sysDictService.getAllDicts();
        if (CollUtil.isNotEmpty(list)) {
            Map<String, List<SysDict>> map = list.stream().collect(Collectors.groupingBy(e -> isBlankDefault(e.getMemo(), e.getDictType())));
            for (String dictType : map.keySet()) {
                List<SysDict> dictList = map.get(dictType);
                Map<String, String> collect = dictList.stream().collect(Collectors.toMap(SysDict::getDictValue, SysDict::getDictLabel));
                RedisUtils.setBeanValue(dictType, collect);
            }
        }
    }

    private String isBlankDefault(String val, String appendStr) {
        if (val == null) {
            return "" + appendStr;
        }else {
            return val + appendStr;
        }
    }

    /**
     * @description 更新字典缓存
     */
    public ResultInfo updateDictCache(SysDict sysDict) {
        try {
            //获取缓存数据
            Map<String, HashMap> dictData = (Map<String, HashMap>) RedisUtils.getBeanValue("dictData");
            if (dictData.containsKey(sysDict.getDictType())) {
                if (StringUtils.equals("1", sysDict.getIsDel() + "")) {
                    HashMap dictMap = dictData.get(sysDict.getDictType());
                    dictMap.remove(sysDict.getDictValue());
                } else {
                    HashMap dictMap = dictData.get(sysDict.getDictType());
                    dictMap.put(sysDict.getDictValue(), sysDict);
                }

            } else {
                HashMap hashMap = new HashMap();
                hashMap.put(sysDict.getDictValue(), sysDict);
                dictData.put(sysDict.getDictType(), hashMap);
            }
            RedisUtils.setBeanValue("dictData", dictData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("字典缓存更新失败！");
        }

        return ResultInfo.success();
    }

    /**
     * 储存所有用户信息
     */
    public void usersInit() {
        List<SysUser> sysUsers = userMapper.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getIsDel, CommonConstants.ISDEL_NO));
        Map<Integer, SysUser> map = new HashMap<>();
        sysUsers.forEach(x -> {
            map.put(x.getId(), x);
        });
        RedisUtils.setBeanValue("userDict", map);
    }

    /**
     * @description 更新字典缓存
     */
    public ResultInfo updateUsersCache(SysUser sysUser) {
        try {
            //获取缓存数据
            Map<Integer, SysUser> dictData = (Map<Integer, SysUser>) RedisUtils.getBeanValue("userDict");
            if (null != dictData.get(sysUser.getId())) {
                if (CommonConstants.ISDEL_YES.equals(sysUser.getIsDel())) {
                    dictData.remove(sysUser.getId());
                } else {
                    dictData.put(sysUser.getId(), sysUser);
                }
            } else {
                dictData.put(sysUser.getId(), sysUser);
            }
            RedisUtils.setBeanValue("userDict", dictData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("字典缓存更新失败！");
        }

        return ResultInfo.success();
    }

    /**
     * @description 用户信息缓存
     */
    public void userInit() {
        SysUser sysUser = userService.getUser();
        Map<Integer, SysUser> map = new HashMap<>();
        if (sysUser != null) {
            map.put(sysUser.getId(), sysUser);
        }
        RedisUtils.setBeanValue("userData", map);
    }


    /**
     * @description 用户信息缓存更新
     */
    public void updateUserCache(SysUser user) {
        Map<Integer, SysUser> userData = (Map<Integer, SysUser>) RedisUtils.getBeanValue("userData");
        if ("1".equals(user.getIsDel())) {
            userData.remove(user.getId());
        } else {
            userData.put(user.getId(), user);
        }
        RedisUtils.setBeanValue("userData", userData);
    }

    /**
     * 把字典数据塞入redis
     */
    public void putAllDict() {
        List<SysDict> dicts = sysDictService.getAllDicts();
        //根据字典类型分组
        Map<String, Map<String, String>> dictMap = new HashMap<>();
        Map<String, List<SysDict>> dictListMap = new HashMap<>();

        //key 是字典类型+字典值   value是label
        Map<String, String> map = new HashMap<>();

        for (SysDict dict : dicts) {
            String type = dict.getDictType();
            Map<String, String> tempMap = dictMap.get(type);
            if (null == tempMap) {
                tempMap = new HashMap<>();
                dictMap.put(type, tempMap);
            }
            tempMap.put(dict.getDictValue(), dict.getDictLabel());

            List<SysDict> dictList = dictListMap.get(type);
            if (null == dictList) {
                dictList = new ArrayList<>();
                dictListMap.put(type, dictList);
            }
            dictList.add(dict);

            map.put(type + "_" + dict.getDictValue(), dict.getDictLabel());
        }

        RedisUtils.setBeanValue(CommonConstants.allDictMapPrefix, dictMap);
        RedisUtils.setBeanValue(CommonConstants.allDictListPrefix, dictListMap);
        //每一种dictType类型的list
        for (String key : dictListMap.keySet()) {
            RedisUtils.setBeanValue(CommonConstants.dictListPrefix + key, dictListMap.get(key));
        }
        RedisUtils.setBeanValue(CommonConstants.allDictKeyPrefix, map);
        System.out.println("===");
    }


}
