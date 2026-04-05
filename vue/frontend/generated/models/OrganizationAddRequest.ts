/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type OrganizationAddRequest = {
    address?: string;
    description?: string;
    leaderId?: number;
    /**
     * 组织编码
     */
    orgCode: string;
    /**
     * 组织级别
     */
    orgLevel: number;
    /**
     * 组织名称
     */
    orgName: string;
    /**
     * 组织类型：党委/党总支/党支部
     */
    orgType: string;
    /**
     * 父组织ID
     */
    parentId: number;
};

