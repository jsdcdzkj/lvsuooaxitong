package com.jsdc.lsoa.vo;

import com.jsdc.lsoa.model.ClientInfo;
import lombok.Data;

@Data
public class ClientInfoVo extends ClientInfo {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
