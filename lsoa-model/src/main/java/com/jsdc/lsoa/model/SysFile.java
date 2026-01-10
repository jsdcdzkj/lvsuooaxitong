package com.jsdc.lsoa.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("SYS_FILE")
@Table(name = "SYS_FILE")
@Data
@Accessors
public class SysFile {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增ID")
    private Integer id;

    /**
     * 业务类型 字典 fileType
     */
    @ApiModelProperty(value = "业务类型")
    private Integer bizType;

    @ApiModelProperty(value = "业务ID")
    private Integer bizId;

    @ApiModelProperty(value = "原始文件名")
    private String originalFilename;

    @ApiModelProperty(value = "新文件名")
    private String filename;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "预览地址")
    private String fileUrl;

    @ApiModelProperty(value = "删除标记（0：否，1：是）")
    private Integer isDel;

    @ApiModelProperty(value = "创建者")
    private Integer createUser;

    @ApiModelProperty(value = "修改者")
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
