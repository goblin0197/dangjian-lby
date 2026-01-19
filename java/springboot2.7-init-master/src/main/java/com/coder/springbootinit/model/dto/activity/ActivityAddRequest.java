package com.coder.springbootinit.model.dto.activity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 活动添加请求
 *
*/
@Data
public class ActivityAddRequest {

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称", required = true )
    private String activityName;

    /**
     * 所属党组织ID
     */
    @ApiModelProperty(value = "所属党组织ID", required = true)
    private Long orgId;

    /**
     * 活动类型:1.会议/2.志愿活动/3.学习/4.其他
     */
    @ApiModelProperty(value = "活动类型:1.会议/2.志愿活动/3.学习/4.其他", required = true)
    private Integer activityType;

    /**
     * 活动描述
     */
    @ApiModelProperty(value = "活动描述")
    private String activityContent;

    /**
     * 报名截止时间
     */
    @ApiModelProperty(value = "报名截止时间")
    private Date enrollDeadline;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;

    /**
     * 活动地点
     */
    @ApiModelProperty(value = "活动地点", required = true)
    private String location;

    /**
     * 最大参与人数
     */
    @ApiModelProperty(value = "最大参与人数", required = true)
    private Integer maxNum;

    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    @ApiModelProperty(value = "活动状态:1.待发布/2.已发布/3.进行中/4.已结束", required = true)
    private Integer status;

    
}