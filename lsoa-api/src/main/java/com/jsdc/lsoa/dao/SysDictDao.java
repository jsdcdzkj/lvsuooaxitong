package com.jsdc.lsoa.dao;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.SysDict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.model.SysDict;
import org.springframework.stereotype.Repository;

@Repository
public class SysDictDao extends BaseDao<SysDict>  {


    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param page
     * @param bean
     * @return
     */
    public String getEntityList(Page page, SysDict bean) {
//        String sql = " SELECT *  "+
//                " FROM @TableName(value = "sys_dict  "+
//                " WHERE "+
//                " 1=1 ";

        return "";
    }
}
