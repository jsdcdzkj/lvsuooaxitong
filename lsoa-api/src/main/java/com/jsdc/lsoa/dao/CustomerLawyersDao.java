package com.jsdc.lsoa.dao;
import com.jsdc.core.base.BaseDao;
import com.jsdc.lsoa.model.CustomerLawyers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.model.CustomerLawyers;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerLawyersDao extends BaseDao<CustomerLawyers>  {


    /**
     * 此方法为默认方法 请添加自己的逻辑代码
     * @param page
     * @param bean
     * @return
     */
    public String getEntityList(Page page, CustomerLawyers bean) {
        String sql = " SELECT *  "+
                " FROM customer_lawyers  "+
                " WHERE "+ 
                " 1=1 ";

        return sql;
    }
}
