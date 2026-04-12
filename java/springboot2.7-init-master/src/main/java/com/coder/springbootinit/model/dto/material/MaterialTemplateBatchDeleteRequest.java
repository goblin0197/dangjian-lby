package com.coder.springbootinit.model.dto.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 材料模板批量删除请求
 *
 */
@Data
public class MaterialTemplateBatchDeleteRequest implements Serializable {

    /**
     * 模板 ID 列表
     */
    @NotEmpty(message = "模板 ID 列表不能为空")
    @ApiModelProperty(value = "模板 ID 列表", required = true)
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}
