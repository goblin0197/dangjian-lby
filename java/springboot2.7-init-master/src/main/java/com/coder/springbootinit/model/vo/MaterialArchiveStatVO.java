package com.coder.springbootinit.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 归档统计 VO
 *
 */
@Data
public class MaterialArchiveStatVO implements Serializable {

    /**
     * 归档总数
     */
    @ApiModelProperty(value = "归档总数")
    private Long totalCount;

    /**
     * 本月归档数
     */
    @ApiModelProperty(value = "本月归档数")
    private Long monthCount;

    /**
     * 月度增长率
     */
    @ApiModelProperty(value = "月度增长率")
    private BigDecimal growthRate;

    /**
     * 各阶段归档统计
     */
    @ApiModelProperty(value = "各阶段归档统计")
    private Object stageStats;

    private static final long serialVersionUID = 1L;
}