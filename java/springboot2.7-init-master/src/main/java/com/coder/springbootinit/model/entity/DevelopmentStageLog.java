package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 发展阶段变更日志表
 *
 */
@TableName(value = "development_stage_log")
@Data
public class DevelopmentStageLog implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发展阶段记录ID
     */
    private Long stageId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 操作类型：创建/更新/提交审核/审核/删除
     */
    private String operationType;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 变更前数据（JSON格式）
     */
    private String beforeData;

    /**
     * 变更后数据（JSON格式）
     */
    private String afterData;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
