package com.jsdc.lsoa.dao;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.SysLog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.model.SysLog;
import org.springframework.stereotype.Repository;

@Repository
public class SysLogDao extends BaseDao<SysLog>  {


    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param page
     * @param bean
     * @return
     */
    public String getEntityList(Page page, SysLog bean) {
        String sql = " SELECT *  "+
                " FROM SYS_LOG  "+
                " WHERE "+ 
                " 1=1 ";

        return sql;
    }
}
