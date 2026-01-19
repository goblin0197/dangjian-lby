package com.coder.springbootinit.model.dto.activityEnroll;

import lombok.Data;

/**
 * 取消活动报名请求
 *
*/
@Data
public class ActivityEnrollCancelRequest {

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;
}