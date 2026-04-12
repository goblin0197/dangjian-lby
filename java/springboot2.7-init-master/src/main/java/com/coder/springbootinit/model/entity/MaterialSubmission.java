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
 * 材料提交实体
 *
 */
@TableName(value = "material_submission")
@Data
public class MaterialSubmission implements Serializable {

    /**
     * 提交 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 组织层级
     */
    private String orgLevel;

    /**
     * 发展阶段
     */
    private String stage;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 提交状态：unsubmitted-未提交/submitted-已提交/approved-审核通过/final_approved-终审通过/rejected-退回/archived-已归档
     */
    private String submitStatus;

    /**
     * 审核状态：pending-待审核/auditing-审核中/approved-通过/rejected-退回
     */
    private String auditStatus;

    /**
     * 材料文件路径
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核人 ID
     */
    private Long auditorId;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 终审人
     */
    private String finalAuditor;

    /**
     * 终审人 ID
     */
    private Long finalAuditorId;

    /**
     * 终审时间
     */
    private Date finalAuditTime;

    /**
     * 终审意见
     */
    private String finalAuditOpinion;

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