package com.jsdc.lsoa.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统字典
 *
 * @author wangYan
 * @since 2023-05-08
 */
@Entity
@TableName(value = "sys_dict")
@Table(name = "sys_dict")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统字典")
public class SysDict extends Model<SysDict> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "系统字典id")
    private Integer id;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "字典类型名称")
    private String dictTypeName;

    @ApiModelProperty(value = "字典名称")
    private String dictLabel;

    @ApiModelProperty(value = "描述")
    private String memo;

    private String sort;

    private String createUser;
    private String updateUser;
    private String createTime;
    private String updateTime;
    private Integer isDel;
}
