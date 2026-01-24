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
 * 用户量化统计
 *
*/
@TableName(value = "user_quantify")
@Data
public class UserQuantify implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 统计日期
     */
    private Date statDate;

    /**
     * 总活动数
     */
    private Integer totalActivity;

    /**
     * 参与活动数
     */
    private Integer participateActivity;

    /**
     * 活动参与度(参与活动数/总活动数)
     */
    private Double participateRate;

    /**
     * 签到活动数
     */
    private Integer signActivity;

    /**
     * 签到率(签到活动数/参与活动数)
     */
    private Double signRate;

    /**
     * 材料文件数量
     */
    private Integer fileCount;

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