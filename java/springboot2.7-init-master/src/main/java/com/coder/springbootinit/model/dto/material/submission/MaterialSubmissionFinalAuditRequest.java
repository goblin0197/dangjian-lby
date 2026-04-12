package com.coder.springbootinit.model.dto.material.submission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料终审请求
 *
 */
@Data
public class MaterialSubmissionFinalAuditRequest implements Serializable {

    /**
     * 提交 ID
     */
    @ApiModelProperty(value = "提交 ID", required = true)
    private Long id;

    /**
     * 终审结果：approved-通过/rejected-退回
     */
    @ApiModelProperty(value = "终审结果：approved-通过/rejected-退回", required = true)
    private String auditResult;

    /**
     * 终审意见
     */
    @ApiModelProperty(value = "终审意见")
    private String auditOpinion;

    private static final long serialVersionUID = 1L;
}