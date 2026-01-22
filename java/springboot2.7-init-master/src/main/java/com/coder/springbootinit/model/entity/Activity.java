package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动
 *
*/
@TableName(value = "activity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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

    /**
     * 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
     */
    private Integer status;

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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}