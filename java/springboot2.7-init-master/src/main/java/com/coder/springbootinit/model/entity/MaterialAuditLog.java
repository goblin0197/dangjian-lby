package com.coder.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 材料审核日志实体
 *
 */
@TableName(value = "material_audit_log")
@Data
public class MaterialAuditLog implements Serializable {

    /**
     * 日志 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 提交 ID
     */
    private Long submissionId;

    /**
     * 审核用户 ID
     */
    private Long auditUserId;

    /**
     * 审核用户姓名
     */
    private String auditUserName;

    /**
     * 审核类型：audit-初审/final_audit-终审
     */
    private String auditType;

    /**
     * 审核结果：approved-通过/rejected-退回
     */
    private String auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}