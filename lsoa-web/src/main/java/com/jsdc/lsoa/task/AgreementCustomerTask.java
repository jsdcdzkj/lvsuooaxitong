package com.jsdc.lsoa.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.mapper.AgreementCustomerMapper;
import com.jsdc.lsoa.model.AgreementCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AgreementCustomerTask {

    @Autowired
    private AgreementCustomerMapper agreementCustomerMapper;

    @Scheduled(cron = "0 0 1 * * ?")
    public void checkCustomerServiceTime() {
        List<AgreementCustomer> list = agreementCustomerMapper.selectList(new LambdaQueryWrapper<AgreementCustomer>()
                .eq(AgreementCustomer::getIsDel, 0)
        );
        Date now = new Date();
        for (AgreementCustomer customer : list) {
            if (now.after(customer.getStarTime()) && now.before(customer.getEndTime())) {
                customer.setInService(1);
            }else {
                customer.setInService(0);
            }
            agreementCustomerMapper.updateById(customer);
        }
    }

}
