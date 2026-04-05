/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type OrgRelationTransferAddRequest = {
    /**
     * 原党组织ID,无组织时为0
     */
    fromOrgId: number;
    /**
     * 目标党组织ID
     */
    toOrgId: number;
    transferReason?: string;
    /**
     * 用户ID（党员ID）
     */
    userId: number;
};

