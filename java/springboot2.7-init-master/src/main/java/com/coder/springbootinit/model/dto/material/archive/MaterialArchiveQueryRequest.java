package com.coder.springbootinit.model.dto.material.archive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料归档查询请求
 *
 */
@Data
public class MaterialArchiveQueryRequest implements Serializable {

    /**
     * 用户姓名（模糊查询）
     */
    @ApiModelProperty(value = "用户姓名（模糊查询）")
    private String userName;

    /**
     * 材料名称（模糊查询）
     */
    @ApiModelProperty(value = "材料名称（模糊查询）")
    private String materialName;

    /**
     * 发展阶段
     */
    @ApiModelProperty(value = "发展阶段")
    private String stage;

    /**
     * 组织层级
     */
    @ApiModelProperty(value = "组织层级")
    private String orgLevel;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", example = "1")
    private Integer current;

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize;

    private static final long serialVersionUID = 1L;
}