package com.coder.springbootinit.model.dto.PartyOrganization;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * 党组织表
 *
 */
@Data
public class PartyOrganizationBindRequest implements Serializable {

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