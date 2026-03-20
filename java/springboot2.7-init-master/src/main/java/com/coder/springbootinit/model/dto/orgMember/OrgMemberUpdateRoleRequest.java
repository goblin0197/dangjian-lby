package com.coder.springbootinit.model.dto.orgMember;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑组织成员角色请求
 *
 */
@Data
public class OrgMemberUpdateRoleRequest implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    private Long orgId;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色", required = true)
    private String userRole;

    private static final long serialVersionUID = 1L;
}
