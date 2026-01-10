package com.jsdc.lsoa.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.NonLitigationBusinessDao;
import com.jsdc.lsoa.model.NonLitigationBusiness;
import com.jsdc.lsoa.vo.NonLitigationBusinessVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface NonLitigationBusinessMapper extends BaseMapper<NonLitigationBusiness> {


    @SelectProvider(type = NonLitigationBusinessDao.class, method = "getPage")
    Page<NonLitigationBusiness> getPage(Page<NonLitigationBusiness> page, NonLitigationBusinessVO vo, boolean hasSpecialPermission);

}
