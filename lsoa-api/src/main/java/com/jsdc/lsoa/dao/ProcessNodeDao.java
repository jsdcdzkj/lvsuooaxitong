package com.jsdc.lsoa.dao;

import cn.hutool.core.util.StrUtil;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.ProcessDesign;
import com.jsdc.lsoa.model.ProcessNode;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.model.SysUser;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ProcessNodeDao extends BaseDao<ProcessNode>  {



    public String  selectByApprover(Integer type, Integer value) {
        StringBuilder sb = new StringBuilder("select id, approverType, approverValue, createTime, createUser, nodeType, processId, sort, updateTime, updateUser from lsoa.process_node");
        sb.append(" where approverType = ").append(type);
        sb.append(" and find_in_set('").append(value).append("', approverValue) ");
        return sb.toString();
    }
}
