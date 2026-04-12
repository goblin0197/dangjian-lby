package com.coder.springbootinit.model.dto.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 材料审核请求
 *
 */
@Data
public class MaterialSubmissionAuditRequest implements Serializable {

    /**
     * 提交 ID
     */
    @NotNull(message = "提交 ID 不能为空")
    @ApiModelProperty(value = "提交 ID", required = true)
    private Long id;

    /**
     * 审核结果：approved-通过/rejected-退回
     */
    @NotBlank(message = "审核结果不能为空")
    @ApiModelProperty(value = "审核结果：approved-通过/rejected-退回", required = true)
    private String auditResult;

    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    private static final long serialVersionUID = 1L;
}
