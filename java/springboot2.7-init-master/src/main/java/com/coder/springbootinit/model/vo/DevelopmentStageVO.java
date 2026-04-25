package com.coder.springbootinit.model.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.coder.springbootinit.model.entity.MyFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 发展阶段表
 *
 */
@Data
public class DevelopmentStageVO implements Serializable {

    /**
     * id
     */
    private Long id;

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
     * 考察内容
     */
    private String assessmentContent;

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
     * 审核意见
     */
    private String auditRemark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String attachment;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private List<MyFile> files;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
