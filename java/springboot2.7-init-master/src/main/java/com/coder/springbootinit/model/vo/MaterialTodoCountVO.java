package com.coder.springbootinit.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料待办数量 VO
 *
 */
@Data
public class MaterialTodoCountVO implements Serializable {

    /**
     * 待提交数量
     */
    @ApiModelProperty(value = "待提交数量")
    private Integer toSubmitCount;

    /**
     * 待审核数量
     */
    @ApiModelProperty(value = "待审核数量")
    private Integer toAuditCount;

    /**
     * 待终审数量
     */
    @ApiModelProperty(value = "待终审数量")
    private Integer toFinalAuditCount;

    private static final long serialVersionUID = 1L;
}