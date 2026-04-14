package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 量化指标实体
 *
 */
@TableName(value = "quantify_indicator")
@Data
public class QuantifyIndicator implements Serializable {

    /**
     * 指标ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 统计规则
     */
    private String rule;

    /**
     * 统计维度：organization-组织/personal-个人/both-两者
     */
    private String dimension;

    /**
     * 适用组织层级，JSON格式
     */
    private String orgLevel;

    /**
     * 状态：enable-启用/disable-停用
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
