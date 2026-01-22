package com.coder.springbootinit.model.vo;

import java.util.Date;
import lombok.Data;

/**
 * 活动VO
 *
*/
@Data
public class ActivityVO {

    /**
     * id
     */
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 所属党组织ID
     */
    private Long orgId;

    /**
     * 所属党组织名称
     */
    private String orgName;

    /**
     * 创建人ID
     */
    private Long userId;

    /**
     * 创建人姓名
     */
    private String userName;

    /**
     * 活动类型:1.会议/2.志愿活动/3.学习/4.其他
     */
    private Integer activityType;

    /**
     * 活动类型名称
     */
    private String activityTypeName;

    /**
     * 活动描述
     */
    private String activityContent;

    /**
     * 报名截止时间
     */
    private Date enrollDeadline;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 最大参与人数
     */
    private Integer maxNum;

    /**
     * 当前参与人数
     */
    private Integer currentNum;

    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    private Integer status;

    /**
     * 活动状态名称
     */
    private String statusName;

    /**
     * 活动总结
     */
    private String reviewContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    // /**
    //  * 是否已报名
    //  */
    // private Boolean isEnrolled;
    
    /**
     * 总参与人数（已报名人数）
     */
    private Integer totalParticipant;

    /**
     * 实际参与人数（已签到人数）
     */
    private Integer actualParticipant;

    /**
     * 签到率（实际参与人数/总参与人数）
     */
    private Double signRate;
}