package com.coder.springbootinit.model.dto.material.submission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料提交请求
 *
 */
@Data
public class MaterialSubmissionSubmitRequest implements Serializable {

    /**
     * 发展阶段
     */
    @ApiModelProperty(value = "发展阶段", required = true)
    private String stage;

    /**
     * 材料名称
     */
    @ApiModelProperty(value = "材料名称", required = true)
    private String materialName;

    /**
     * 材料文件路径
     */
    @ApiModelProperty(value = "材料文件路径", required = true)
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}