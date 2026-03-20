package com.coder.springbootinit.model.dto.developmentStage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发展阶段更新请求
 *
 */
@Data
public class DevelopmentStageUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 阶段结束时间
     */
    @ApiModelProperty(value = "阶段结束时间")
    private Date stageEndTime;

    /**
     * 阶段状态：0进行中/1已完成/2已终止
     */
    @ApiModelProperty(value = "阶段状态")
    private Integer stageStatus;

    /**
     * 考察内容
     */
    @ApiModelProperty(value = "考察内容")
    private String assessmentContent;

    /**
     * 考察结果：1合格/0不合格
     */
    @ApiModelProperty(value = "考察结果")
    private Integer assessmentResult;

    private static final long serialVersionUID = 1L;
}
