package com.coder.springbootinit.model.dto.material.archive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料归档请求
 *
 */
@Data
public class MaterialArchiveRequest implements Serializable {

    /**
     * 提交 ID
     */
    @ApiModelProperty(value = "提交 ID", required = true)
    private Long submissionId;

    /**
     * 归档备注
     */
    @ApiModelProperty(value = "归档备注")
    private String archiveRemark;

    private static final long serialVersionUID = 1L;
}