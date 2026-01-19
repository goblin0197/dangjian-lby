package com.coder.springbootinit.model.dto.activity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 活动更新请求
 *
*/
@Data
public class ActivityUpdateRequest {

    /**
     * id
     */
    @ApiModelProperty(value = "活动ID", required = true)
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 所属党组织ID
     */
    @ApiModelProperty(value = "所属党组织ID")
    private Long orgId;

    /**
     * 活动类型:1.会议/2.志愿活动/3.学习/4.其他
     */
    @ApiModelProperty(value = "活动类型:1.会议/2.志愿活动/3.学习/4.其他")
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
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 活动地点
     */
    @ApiModelProperty(value = "活动地点")
    private String location;

    /**
     * 最大参与人数
     */
    @ApiModelProperty(value = "最大参与人数")
    private Integer maxNum;

    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    @ApiModelProperty(value = "活动状态:1.待发布/2.已发布/3.进行中/4.已结束")
    private Integer status;

    /**
     * 活动总结
     */
    @ApiModelProperty(value = "活动总结")
    private String reviewContent;
}