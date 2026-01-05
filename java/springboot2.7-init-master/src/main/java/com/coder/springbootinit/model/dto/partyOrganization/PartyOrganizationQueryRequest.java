package com.coder.springbootinit.model.dto.partyOrganization;

import com.coder.springbootinit.common.PageRequest;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 党组织表
 *
 */
@Data
public class PartyOrganizationQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 组织类型：党委/党总支/党支部
     */
    private String orgType;

    /**
     * 组织级别
     */
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}