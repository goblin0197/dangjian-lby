package com.coder.springbootinit.model.dto.user;

import com.coder.springbootinit.common.PageRequest;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户角色：super_admin/org_admin/party_member/activist_development
     */
    private String userRole;

    /**
     * 所属党组织ID
     */
    private Long orgId;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型：教师/学生
     */
    private String userType;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 申请入党日期
     */
    private Date joinDate;

    /**
     * 转正日期
     */
    private Date positiveDate;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    private static final long serialVersionUID = 1L;
}