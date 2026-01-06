package com.coder.springbootinit.model.dto.organization;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织绑定请求
 */
@Data
public class OrganizationBindRequest implements Serializable {

    /**
     * 党组织ID
     */
    @ApiModelProperty(value = "党组织ID", required = true)
    private Long orgId;

    /**
     * 负责人ID
     */
    @ApiModelProperty(value = "负责人ID", required = true)
    private Long leaderId;

    private static final long serialVersionUID = 1L;
}