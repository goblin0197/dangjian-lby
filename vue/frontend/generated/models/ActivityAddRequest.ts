/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ActivityAddRequest = {
    /**
     * 活动描述
     */
    activityContent?: string;
    /**
     * 活动名称
     */
    activityName: string;
    /**
     * 活动类型:1.会议/2.志愿活动/3.学习/4.其他
     */
    activityType: number;
    /**
     * 结束时间
     */
    endTime: string;
    /**
     * 报名截止时间
     */
    enrollDeadline?: string;
    /**
     * 活动地点
     */
    location: string;
    /**
     * 最大参与人数
     */
    maxNum: number;
    /**
     * 所属党组织ID
     */
    orgId: number;
    /**
     * 开始时间
     */
    startTime: string;
    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    status: number;
};

