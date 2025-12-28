package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 组织关系转移表
 *
 */
@TableName(value = "party_relation_transfer")
@Data
public class PartyRelationTransfer implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 转移类型：转入/转出/内部调整
     */
    private String transferType;

    /**
     * 转移原因
     */
    private String transferReason;

    /**
     * 转移时间
     */
    private Date transferTime;

    /**
     * 审批状态：待审批/已通过/已拒绝
     */
    private String approveStatus;

    /**
     * 审批人ID
     */
    private Long approveUserId;

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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}