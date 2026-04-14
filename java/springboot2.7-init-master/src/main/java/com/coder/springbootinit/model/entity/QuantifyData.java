package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 量化数据记录实体
 *
 */
@TableName(value = "quantify_data")
@Data
public class QuantifyData implements Serializable {

    /**
     * 数据ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 指标ID
     */
    private Long indicatorId;

    /**
     * 统计对象ID
     */
    private Long targetId;

    /**
     * 统计对象类型
     */
    private String targetType;

    /**
     * 统计周期
     */
    private String period;

    /**
     * 统计值
     */
    private BigDecimal value;

    /**
     * 活动参与率
     */
    private BigDecimal activityRate;

    /**
     * 签到率
     */
    private BigDecimal signRate;

    /**
     * 材料完成率
     */
    private BigDecimal materialRate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
