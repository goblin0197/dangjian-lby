package com.coder.springbootinit.model.dto.developmentStage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发展阶段添加请求
 *
 */
@Data
public class DevelopmentStageAddRequest implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 培养人ID
     */
    @ApiModelProperty(value = "培养人ID", required = true)
    private Long trainerId;

    /**
     * 阶段名称：积极分子/发展对象/预备党员/正式党员
     */
    @ApiModelProperty(value = "阶段名称", required = true)
    private String stageName;

    /**
     * 阶段开始时间
     */
    @ApiModelProperty(value = "阶段开始时间", required = true)
    private Date stageStartTime;

    /**
     * 考察内容
     */
    @ApiModelProperty(value = "考察内容")
    private String assessmentContent;

    private static final long serialVersionUID = 1L;
}
