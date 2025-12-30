package com.coder.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织关系转移表
 *
 */
@Data
public class PartyRelationTransferVO implements Serializable {

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
     * 原党组织名称
     */
    private String fromPartyName;

    /**
     * 目标党组织ID
     */
    private Long toPartyId;

    /**
     * 目标党组织名称
     */
    private String toPartyName;

    /**
     * 转移原因
     */
    private String transferReason;

    /**
     * 转移时间
     */
    private Date transferTime;

    /**
     * 审批状态：1-待审批/2-已通过/3-已拒绝
     */
    private Integer approveStatus;

    /**
     * 审批人ID
     */
    private Long approveUserId;

    /**
     * 审批人昵称
     */
    private String approveUserName;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 审批意见
     */
    private String approveComment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}