package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollAddRequest;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollCancelRequest;
import com.coder.springbootinit.model.entity.ActivityEnroll;

/**
 * 活动报名服务
 *
*/
public interface ActivityEnrollService extends IService<ActivityEnroll> {

    /**
     * 报名活动
     * @param activityEnrollAddRequest 活动报名请求
     * @param opUserId 操作人ID
     * @return 是否成功
     */
    boolean enrollActivity(ActivityEnrollAddRequest activityEnrollAddRequest, Long opUserId);

    /**
     * 取消报名
     * @param activityEnrollCancelRequest 取消报名请求
     * @param opUserId 操作人ID
     * @return 是否成功
     */
    boolean cancelEnroll(ActivityEnrollCancelRequest activityEnrollCancelRequest, Long opUserId);

    /**
     * 签到
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param opUserId 操作人ID
     * @return 是否成功
     */
    boolean signInActivity(Long activityId, Long userId, Long opUserId);

    /**
     * 检查是否已报名
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 是否已报名
     */
    boolean checkEnrolled(Long activityId, Long userId);

    /**
     * 获取报名状态
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 报名状态
     */
    Integer getEnrollStatus(Long activityId, Long userId);
}