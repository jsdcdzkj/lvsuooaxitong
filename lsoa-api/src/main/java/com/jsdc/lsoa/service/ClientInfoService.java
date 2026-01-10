package com.jsdc.lsoa.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.dao.ClientInfoDao;
import com.jsdc.lsoa.mapper.ClientInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.lsoa.model.ClientInfo;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.ClientInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientInfoService extends ServiceImpl<ClientInfoMapper,ClientInfo>  {

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    /**
     * 分页查询 todo
     *
     * @return
     */
    public Page<ClientInfo> getPageList(ClientInfoVo vo) {
        QueryWrapper<ClientInfo> queryWrapper = new QueryWrapper<>();

        //Page<ClientInfoVo> pageList = clientInfoMapper.getPageList(new Page<>(vo.getPageNum(), vo.getPageSize()), vo);
        return clientInfoMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), queryWrapper);

    }

    /**
     * 查询 todo
     *
     * @return
     */
    public List<ClientInfo> getList(ClientInfo entity) {
        LambdaQueryWrapper<ClientInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClientInfo::getCaseId, entity.getCaseId())
                .eq(StrUtil.isNotBlank(entity.getPlainOrDefend()),ClientInfo::getPlainOrDefend,entity.getPlainOrDefend())
                .eq(StrUtil.isNotBlank(entity.getIsPerson()),ClientInfo::getIsPerson,entity.getIsPerson())
                .eq(ClientInfo::getIsDel,0)
                .orderByAsc(ClientInfo::getId);
        return clientInfoMapper.selectList(queryWrapper);
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    public ResultInfo saveOrUpdateClientInfo(ClientInfo bean) {
        saveOrUpdate(bean);
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

}


