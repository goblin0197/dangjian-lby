package com.coder.springbootinit.model.dto.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 材料归档批量导出请求
 *
 */
@Data
public class MaterialArchiveBatchExportRequest implements Serializable {

    /**
     * 归档 ID 列表
     */
    @NotEmpty(message = "归档 ID 列表不能为空")
    @ApiModelProperty(value = "归档 ID 列表", required = true)
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}
