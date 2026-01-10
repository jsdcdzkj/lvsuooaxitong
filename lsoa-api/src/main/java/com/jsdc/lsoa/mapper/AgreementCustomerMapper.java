package com.jsdc.lsoa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.dao.AgreementCustomerDao;
import com.jsdc.lsoa.model.AgreementCustomer;
import com.jsdc.lsoa.vo.AgreementCustomerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


@Mapper
public interface AgreementCustomerMapper extends BaseMapper<AgreementCustomer> {
    @SelectProvider(type = AgreementCustomerDao.class, method = "getEntityList")
    Page<AgreementCustomerVO> getEntityList(Page page, AgreementCustomerVO vo);
    @SelectProvider(type = AgreementCustomerDao.class, method = "getEntityListByUser")
    Page<AgreementCustomerVO> getEntityListByUser(Page page, AgreementCustomerVO vo);
    @SelectProvider(type = AgreementCustomerDao.class, method = "getList")
    List<AgreementCustomer> getList(Integer id);

    @SelectProvider(type = AgreementCustomerDao.class, method = "getPage")
    Page<AgreementCustomerVO> getPage(Page page, AgreementCustomerVO vo);

}
