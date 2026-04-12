package com.coder.springbootinit.model.dto.material.template;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料模板更新请求
 *
 */
@Data
public class MaterialTemplateUpdateRequest implements Serializable {

    /**
     * 模板 ID
     */
    @ApiModelProperty(value = "模板 ID", required = true)
    private Long id;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;

    /**
     * 所属发展阶段
     */
    @ApiModelProperty(value = "所属发展阶段")
    private String stage;

    /**
     * 材料类型
     */
    @ApiModelProperty(value = "材料类型")
    private String type;

    /**
     * 模板状态：enable-启用/disable-停用
     */
    @ApiModelProperty(value = "模板状态：enable-启用/disable-停用")
    private String status;

    /**
     * 模板文件路径
     */
    @ApiModelProperty(value = "模板文件路径")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;

    /**
     * 模板说明
     */
    @ApiModelProperty(value = "模板说明")
    private String remark;

    private static final long serialVersionUID = 1L;
}