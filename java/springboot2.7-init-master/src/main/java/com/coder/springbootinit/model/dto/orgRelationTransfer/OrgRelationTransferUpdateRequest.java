package com.coder.springbootinit.model.dto.orgRelationTransfer;

import com.coder.springbootinit.constant.OrgRelationTransferConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织关系转移表 更新请求
 *
 */
@Data
public class OrgRelationTransferUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户ID（党员ID）
     */
    @ApiModelProperty(value = "用户ID（党员ID）", required = true)
    private Long userId;

    /**
     * 原党组织ID
     */
    @ApiModelProperty(value = "原党组织ID", required = true)
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

    /**
     * 转移时间
     */
    private Date transferTime;

    private static final long serialVersionUID = 1L;
}