/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type OrgRelationTransferApproveRequest = {
    /**
     * 审批意见
     */
    approveComment?: string;
    /**
     * 审批状态：1-待审批/2-已通过/3-已拒绝
     */
    approveStatus: number;
    /**
     * 组织转移记录id
     */
    id: number;
};

