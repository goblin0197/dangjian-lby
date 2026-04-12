package com.coder.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 核心指标数据视图对象
 *
 */
@Data
public class QuantifyCoreIndicatorVO {

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
     * 统计时间范围
     */
    private String timeRange;

    /**
     * 组织层级
     */
    private String orgLevel;
}