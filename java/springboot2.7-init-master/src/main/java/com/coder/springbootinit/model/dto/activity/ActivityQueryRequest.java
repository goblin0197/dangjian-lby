package com.coder.springbootinit.model.dto.activity;

import com.coder.springbootinit.common.PageRequest;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动查询请求
 *
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityQueryRequest extends PageRequest {

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
     * 创建人ID
     */
    private Long userId;

    /**
     * 活动类型:1.会议/2.志愿活动/3.学习/4.其他
     */
    private Integer activityType;

    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    private Integer status;

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
}