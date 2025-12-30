package com.coder.springbootinit.model.dto.PartyRelationTransfer;

import com.coder.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织关系转移表 查询请求
 *
 */
@Data
public class PartyRelationTransferQueryRequest extends PageRequest implements Serializable {

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
    private Long fromPartyId;

    /**
     * 目标党组织ID
     */
    private Long toPartyId;

    

    /**
     * 审批状态：1-待审批/2-已通过/3-已拒绝
     */
    private Integer approveStatus;

    /**
     * 审批人ID
     */
    private Long approveUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间（起始）
     */
    private Date createTimeStart;

    /**
     * 创建时间（结束）
     */
    private Date createTimeEnd;

    private static final long serialVersionUID = 1L;
}