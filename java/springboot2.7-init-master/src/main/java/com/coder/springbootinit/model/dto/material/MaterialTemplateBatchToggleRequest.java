package com.coder.springbootinit.model.dto.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 材料模板批量切换状态请求
 *
 */
@Data
public class MaterialTemplateBatchToggleRequest implements Serializable {

    /**
     * 模板 ID 列表
     */
    @NotEmpty(message = "模板 ID 列表不能为空")
    @ApiModelProperty(value = "模板 ID 列表", required = true)
    private List<Long> ids;

    /**
     * 新状态：enable-启用/disable-停用
     */
    @NotBlank(message = "状态不能为空")
    @ApiModelProperty(value = "新状态：enable-启用/disable-停用", required = true)
    private String status;

    private static final long serialVersionUID = 1L;
}
