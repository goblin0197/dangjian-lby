package com.coder.springbootinit.model.dto.material;

import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 材料提交查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialSubmissionQueryRequest extends PageRequest implements Serializable {

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
     * 提交状态：unsubmitted-未提交/submitted-已提交/approved-审核通过/final_approved-终审通过/rejected-退回/archived-已归档
     */
    @ApiModelProperty(value = "提交状态：unsubmitted-未提交/submitted-已提交/approved-审核通过/final_approved-终审通过/rejected-退回/archived-已归档")
    private String submitStatus;

    /**
     * 审核状态：pending-待审核/auditing-审核中/approved-通过/rejected-退回
     */
    @ApiModelProperty(value = "审核状态：pending-待审核/auditing-审核中/approved-通过/rejected-退回")
    private String auditStatus;

    private static final long serialVersionUID = 1L;
}
