package com.coder.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 活动已报名用户视图
 *
 */
@Data
public class ActivityEnrollUserVO implements Serializable {

    /**
     * 报名记录ID
     */
    private Long enrollId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 所属党组织ID
     */
    private Long orgId;

    /**
     * 所属党组织名称
     */
    private String orgName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 报名时间
     */
    private Date enrollTime;

    /**
     * 参与状态:1.已报名/2.已取消
     */
    private Integer participantStatus;

    /**
     * 参与状态名称
     */
    private String participantStatusName;

    /**
     * 是否签到:0.未签到/1.已签到
     */
    private Integer isSign;

    /**
     * 签到状态名称
     */
    private String isSignName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
