package com.coder.springbootinit.model.dto.user;

import java.io.Serializable;

import com.coder.springbootinit.constant.UserConstant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户创建请求
 *
*/
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true)
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: super_admin, org_admin, org_member, activist_development
     */
    @ApiModelProperty(value = "用户角色, super_admin, org_admin, org_member, activist_development")
    private String userRole = UserConstant.ACTIVIST_DEVELOPMENT_ROLE;

    /**
     * 用户类型: 教师/学生
     */
    @ApiModelProperty(value = "用户类型, 教师/学生")
    private String userType = UserConstant.USER_TYPE_STUDENT;

    private static final long serialVersionUID = 1L;
}