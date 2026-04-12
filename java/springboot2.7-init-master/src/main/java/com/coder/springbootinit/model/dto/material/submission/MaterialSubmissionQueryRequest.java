package com.coder.springbootinit.model.dto.material.submission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料提交查询请求
 *
 */
@Data
public class MaterialSubmissionQueryRequest implements Serializable {

    /**
     * 用户 ID
     */
    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    /**
     * 用户姓名（模糊查询）
     */
    @ApiModelProperty(value = "用户姓名（模糊查询）")
    private String userName;

    /**
     * 组织层级
     */
    @ApiModelProperty(value = "组织层级")
    private String orgLevel;

    /**
     * 发展阶段
     */
    @ApiModelProperty(value = "发展阶段")
    private String stage;

    /**
     * 材料名称（模糊查询）
     */
    @ApiModelProperty(value = "材料名称（模糊查询）")
    private String materialName;

    /**
     * 提交状态
     */
    @ApiModelProperty(value = "提交状态")
    private String submitStatus;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

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