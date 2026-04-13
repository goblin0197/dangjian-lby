package com.coder.springbootinit.model.dto.trainerRelation;

import com.baomidou.mybatisplus.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 培养人关联表
 *
 */
@Data
public class TrainerRelationAddRequest implements Serializable {

    /**
     * 用户ID（被培养人）
     */
    @ApiModelProperty(value = "用户ID（被培养人）", required = true)
    private Long userId;

    /**
     * 培养人ID
     */
    @ApiModelProperty(value = "培养人ID", required = true)
    private Long trainerId;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    private static final long serialVersionUID = 1L;
}