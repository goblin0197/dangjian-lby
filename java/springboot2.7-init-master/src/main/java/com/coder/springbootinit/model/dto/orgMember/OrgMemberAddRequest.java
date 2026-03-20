package com.coder.springbootinit.model.dto.orgMember;

import java.io.Serializable;

import com.coder.springbootinit.constant.UserConstant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加组织成员请求
 *
 */
@Data
public class OrgMemberAddRequest implements Serializable {

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    private Long orgId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    private static final long serialVersionUID = 1L;
}
