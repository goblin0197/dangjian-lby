package com.coder.springbootinit.model.dto.activityEnroll;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityEnrollSignInRequest{
    @ApiModelProperty(value = "活动ID", required = true)
    private Long activityId;
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
}
