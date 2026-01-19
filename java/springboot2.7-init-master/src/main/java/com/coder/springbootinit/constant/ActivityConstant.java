package com.coder.springbootinit.constant;

/**
 * 活动常量
 *
 */
public interface ActivityConstant {

    // region 是否签到

    /**
     * 已签到
     */
    Integer SIGNED = 1;

    /**
     * 未签到
     */
    Integer NOT_SIGNED = 0;

    // endregion

    // region 活动类型

    /**
     * 会议
     */
    Integer ACTIVITY_TYPE_MEETING = 1;

    /**
     * 志愿活动
     */
    Integer ACTIVITY_TYPE_VOLUNTEER = 2;

    /**
     * 学习
     */
    Integer ACTIVITY_TYPE_STUDY = 3;

    /**
     * 其他
     */
    Integer ACTIVITY_TYPE_OTHER = 4;

    // endregion

    // region 活动状态

    /**
     * 待发布
     */
    Integer ACTIVITY_STATUS_PENDING = 1;

    /**
     * 已发布
     */
    Integer ACTIVITY_STATUS_PUBLISHED = 2;

    /**
     * 进行中
     */
    Integer ACTIVITY_STATUS_IN_PROGRESS = 3;

    /**
     * 已结束
     */
    Integer ACTIVITY_STATUS_ENDED = 4;

    // endregion

    // region 活动参加情况

    /**
     * 已报名
     */
    Integer ENROLL_STATUS_ENROLLED = 1;

    /**
     * 已取消
     */
    Integer ENROLL_STATUS_CANCELLED = 2;

    // endregion
}