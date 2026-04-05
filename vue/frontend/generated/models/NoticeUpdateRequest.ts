/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type NoticeUpdateRequest = {
    /**
     * 公告内容
     */
    content?: string;
    /**
     * 过期时间
     */
    expireTime?: string;
    /**
     * 公告ID
     */
    id: number;
    /**
     * 是否置顶:0-否,1-是
     */
    isTop?: number;
    /**
     * 状态：0-草稿，1-已发布
     */
    status?: number;
    /**
     * 公告标题
     */
    title?: string;
};

