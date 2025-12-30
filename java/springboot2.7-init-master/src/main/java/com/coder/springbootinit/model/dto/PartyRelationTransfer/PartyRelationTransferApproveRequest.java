package com.coder.springbootinit.model.dto.PartyRelationTransfer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织关系转移表 审批请求
 *
 */
@Data
public class PartyRelationTransferApproveRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "组织转移记录id", required = true)
    private Long id;

    /**
     * 审批状态：1-待审批/2-已通过/3-已拒绝
     */
    @ApiModelProperty(value = "审批状态：1-待审批/2-已通过/3-已拒绝", required = true)
    private Integer approveStatus;

    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String approveComment;

    private static final long serialVersionUID = 1L;
}