package com.coder.springbootinit.model.dto.activityEnroll;

import lombok.Data;

/**
 * 活动已报名人员列表请求
 *
 */
@Data
public class ActivityEnrollListRequest {

    /**
     * 活动ID
     */
    private Long activityId;
}
