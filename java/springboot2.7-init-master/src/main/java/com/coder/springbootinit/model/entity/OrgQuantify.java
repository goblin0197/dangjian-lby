package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 组织量化统计
 *
*/
@TableName(value = "org_quantify")
@Data
public class OrgQuantify implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 统计日期
     */
    private Date statDate;

    /**
     * 活动次数
     */
    private Integer activityCount;

    /**
     * 总参与人数
     */
    private Integer totalParticipant;

    /**
     * 总签到人数
     */
    private Integer totalSign;

    /**
     * 平均每次活动参与人数
     */
    private Double avgParticipant;

    /**
     * 平均每次活动签到人数
     */
    private Double avgSign;

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