package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.NonLitigationBusiness;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NonLitigationBusinessVO extends NonLitigationBusiness {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String beginDate;

    private String endDate;

    private Integer loginUser;

}
