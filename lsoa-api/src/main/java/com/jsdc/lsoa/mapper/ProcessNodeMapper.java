package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.lsoa.dao.ProcessDesignDao;
import com.jsdc.lsoa.dao.ProcessNodeDao;
import com.jsdc.lsoa.model.ProcessNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ProcessNodeMapper extends BaseMapper<ProcessNode> {

    /**
     *
     * @param type 类型 1：人员，2：角色
     * @param value value
     */
    @SelectProvider(type = ProcessNodeDao.class, method = "selectByApprover")
    List<ProcessNode> selectByApprover(Integer type, Integer value);
}
