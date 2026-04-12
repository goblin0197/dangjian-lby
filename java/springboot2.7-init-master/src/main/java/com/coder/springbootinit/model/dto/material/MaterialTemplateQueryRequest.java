package com.coder.springbootinit.model.dto.material;

import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 材料模板查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialTemplateQueryRequest extends PageRequest implements Serializable {

    /**
     * 模板名称（模糊查询）
     */
    @ApiModelProperty(value = "模板名称（模糊查询）")
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

    private static final long serialVersionUID = 1L;
}
