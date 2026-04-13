package com.coder.springbootinit.model.dto.developmentStage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
    private Long userId;

    /**
     * 培养人ID
     */
    private Long trainerId;

    /**
     * 阶段名称：积极分子/发展对象/预备党员/正式党员
     */
    private String stageName;

    /**
     * 阶段开始时间
     */
    private Date stageStartTime;

    /**
     * 阶段结束时间
     */
    private Date stageEndTime;

    /**
     * 阶段状态：0进行中/1已完成/2已终止
     */
    private Integer stageStatus;


    /**
     * 考察结果：1合格/0不合格/2未审核
     */
    private Integer assessmentResult;

    /**
     * 审核人员ID
     */
    private Long auditUserId;

    /**
     * 审核时间
     */
    private Date auditTime;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
