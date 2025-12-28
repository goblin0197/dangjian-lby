package com.coder.springbootinit.constant;

/**
 * 用户常量
 *
*/
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 党建相关角色

    /**
     * 超级管理员角色
     */
    String SUPER_ADMIN_ROLE = "super_admin";

    /**
     * 组织管理员角色
     */
    String ORG_ADMIN_ROLE = "org_admin";

    /**
     * 党员角色
     */
    String PARTY_MEMBER_ROLE = "party_member";

    /**
     * 积极分子/发展对象角色
     */
    String ACTIVIST_DEVELOPMENT_ROLE = "activist_development";

    // endregion

    // region  用户类型

    String USER_TYPE_STUDENT = "学生";

    String USER_TYPE_TEACHER = "教师";
    // endregion

    // region 用户政治面貌

    String POLITICAL_STATUS_PARTY_MEMBER = "党员";
    String POLITICAL_STATUS_PROBATIONARY_PARTY_MEMBER = "预备党员";
    String POLITICAL_STATUS_TEAM_UNION_MEMBER = "共青团员";
    // endregion
}
