package com.coder.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 量化统计数据视图对象
 *
 */
@Data
public class QuantifyStatisticsVO {

    /**
     * 目标ID（组织ID或用户ID）
     */
    private Long targetId;

    /**
     * 目标名称（组织名称或用户名）
     */
    private String targetName;

    /**
     * 统计指标
     */
    private String indicator;

    /**
     * 指标值
     */
    private BigDecimal value;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 统计周期
     */
    private String period;
}