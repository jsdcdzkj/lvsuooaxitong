package com.jsdc.lsoa.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadVO {


    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "业务类型")
    private Integer bizType;

    @ApiModelProperty(value = "业务ID")
    private Integer bizId;

}
