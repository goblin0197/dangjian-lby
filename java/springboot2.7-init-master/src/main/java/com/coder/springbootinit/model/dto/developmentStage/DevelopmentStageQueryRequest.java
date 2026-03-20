package com.coder.springbootinit.model.dto.developmentStage;

import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 发展阶段查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DevelopmentStageQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    /**
     * 阶段名称
     */
    @ApiModelProperty(value = "阶段名称")
    private String stageName;

    /**
     * 阶段状态
     */
    @ApiModelProperty(value = "阶段状态")
    private Integer stageStatus;

    private static final long serialVersionUID = 1L;
}
