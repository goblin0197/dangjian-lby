package com.coder.springbootinit.model.dto.orgMember;

import com.coder.springbootinit.common.PageRequest;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织成员查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrgMemberQueryRequest extends PageRequest implements Serializable {

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    private Long orgId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色")
    private String userRole;

    /**
     * 用户类型：教师/学生
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /**
     * 政治面貌
     */
    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;

    /**
     * 状态：0-正常，1-禁用
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    private static final long serialVersionUID = 1L;
}
