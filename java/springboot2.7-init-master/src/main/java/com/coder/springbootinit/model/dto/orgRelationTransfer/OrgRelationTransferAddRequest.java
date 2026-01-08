package com.coder.springbootinit.model.dto.orgRelationTransfer;

import com.coder.springbootinit.constant.OrgRelationTransferConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织关系转移表 添加请求
 *
 */
@Data
public class OrgRelationTransferAddRequest implements Serializable {

    /**
     * 用户ID（党员ID）
     */
    @ApiModelProperty(value = "用户ID（党员ID）", required = true)
    private Long userId;

    /**
     * 原党组织ID
     */
    @ApiModelProperty(value = "原党组织ID,无组织时为0", required = true)
    private Long fromOrgId = OrgRelationTransferConstant.DEFAULT_ORG_ID;

    /**
     * 目标党组织ID
     */
    @ApiModelProperty(value = "目标党组织ID", required = true)
    private Long toOrgId;


    /**
     * 转移原因
     */
    private String transferReason;

    private static final long serialVersionUID = 1L;
}