package com.coder.springbootinit.model.dto.material.template;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 材料模板批量操作请求
 *
 */
@Data
public class MaterialTemplateBatchRequest implements Serializable {

    /**
     * 模板 ID 列表
     */
    @ApiModelProperty(value = "模板 ID 列表", required = true)
    private List<Long> ids;

    /**
     * 目标状态（用于批量切换状态）
     */
    @ApiModelProperty(value = "目标状态")
    private String targetStatus;

    private static final long serialVersionUID = 1L;
}