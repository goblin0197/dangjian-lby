package com.coder.springbootinit.model.dto.partyOrganization;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


import lombok.Data;

/**
 * 党组织表
 *
 */
@Data
public class PartyOrganizationAddRequest implements Serializable {

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称", required = true)
    private String orgName;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码", required = true)
    private String orgCode;

    /**
     * 父组织ID
     */
    @ApiModelProperty(value = "父组织ID", required = true)
    private Long parentId;

    /**
     * 组织类型：党委/党总支/党支部
     */
    @ApiModelProperty(value = "组织类型：党委/党总支/党支部", required = true)
    private String orgType;

    /**
     * 组织级别
     */
    @ApiModelProperty(value = "组织级别", required = true)
    private Integer orgLevel;

    /**
     * 负责人ID
     */
    private Long leaderId;


    /**
     * 地址
     */
    private String address;

    /**
     * 组织描述
     */
    private String description;

    private static final long serialVersionUID = 1L;
}