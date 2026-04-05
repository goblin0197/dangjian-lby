/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type UserAddRequest = {
    /**
     * 账号
     */
    userAccount: string;
    userAvatar?: string;
    userName?: string;
    /**
     * 用户角色, super_admin, org_admin, org_member, activist_development
     */
    userRole?: string;
    /**
     * 用户类型, 教师/学生
     */
    userType?: string;
};

