package com.coder.springbootinit.model.dto.activityEnroll;

import lombok.Data;

/**
 * 活动报名请求
 *
*/
@Data
public class ActivityEnrollAddRequest {

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;
}