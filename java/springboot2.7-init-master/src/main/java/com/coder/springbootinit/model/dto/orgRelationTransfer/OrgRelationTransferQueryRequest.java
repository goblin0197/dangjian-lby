package com.coder.springbootinit.model.dto.orgRelationTransfer;

import com.coder.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织关系转移表 查询请求
 *
 */
@Data
public class OrgRelationTransferQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID（党员ID）
     */
    private Long userId;

    /**
     * 原党组织ID
     */
    private Long fromOrgId;

    /**
     * 目标党组织ID
     */
    private Long toOrgId;

    /**
     * 原党组织名称
     */
    private String fromOrgName;

    /**
     * 目标党组织名称
     */
    private String toOrgName;

    /**
     * 审批状态：1-待审批/2-已通过/3-已拒绝
     */
    private Integer approveStatus;

    /**
     * 审批人ID
     */
    private Long approveUserId;

    /**
     * 审批人姓名
     */
    private String approveUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}