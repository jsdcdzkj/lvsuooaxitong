package com.jsdc.lsoa.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jsdc.core.base.BaseService;
import com.jsdc.lsoa.dao.CustomerLawyersDao;
import com.jsdc.lsoa.mapper.CustomerLawyersMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.lsoa.model.CustomerLawyers;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.CustomerLawyersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerLawyersService extends BaseService<CustomerLawyers>  {

    @Autowired
    private CustomerLawyersMapper customerLawyersMapper;

    /**
     * 分页查询 todo
     *
     * @return
     */
    public Page<CustomerLawyers> getPageList(CustomerLawyersVo vo) {
        QueryWrapper<CustomerLawyers> queryWrapper = new QueryWrapper<>();

        //Page<CustomerLawyersVo> pageList = customerLawyersMapper.getPageList(new Page<>(vo.getPageNum(), vo.getPageSize()), vo);
        return customerLawyersMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), queryWrapper);

    }

    /**
     * 查询 todo
     *
     * @return
     */
    public List<CustomerLawyers> getList(CustomerLawyers entity) {
        QueryWrapper<CustomerLawyers> queryWrapper = new QueryWrapper<>();
        return customerLawyersMapper.selectList(queryWrapper);
    }

    /**
     * 添加/编辑 todo
     *
     * @param bean
     * @return
     */
    public ResultInfo saveOrUpdateCustomerLawyers(CustomerLawyers bean) {
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


