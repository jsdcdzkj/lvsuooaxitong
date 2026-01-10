package com.jsdc.lsoa.dao;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.ClientInfo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.model.ClientInfo;
import org.springframework.stereotype.Repository;

@Repository
public class ClientInfoDao extends BaseDao<ClientInfo>  {


    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param page
     * @param bean
     * @return
     */
    public String getEntityList(Page page, ClientInfo bean) {
        String sql = " SELECT *  "+
                " FROM client_info  "+
                " WHERE "+ 
                " 1=1 ";

        return sql;
    }
}
