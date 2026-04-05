/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type OrgRelationTransferUpdateRequest = {
    /**
     * 原党组织ID
     */
    fromOrgId: number;
    /**
     * id
     */
    id: number;
    /**
     * 目标党组织ID
     */
    toOrgId: number;
    transferReason?: string;
    transferTime?: string;
    /**
     * 用户ID（党员ID）
     */
    userId: number;
};

