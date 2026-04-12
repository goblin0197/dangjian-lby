package com.coder.springbootinit.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 材料提交 VO
 *
 */
@Data
public class MaterialSubmissionVO implements Serializable {

    /**
     * 提交 ID
     */
    @ApiModelProperty(value = "提交 ID")
    private Long id;

    /**
     * 用户 ID
     */
    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    /**
     * 组织层级
     */
    @ApiModelProperty(value = "组织层级")
    private String orgLevel;

    /**
     * 发展阶段
     */
    @ApiModelProperty(value = "发展阶段")
    private String stage;

    /**
     * 材料名称
     */
    @ApiModelProperty(value = "材料名称")
    private String materialName;

    /**
     * 提交状态
     */
    @ApiModelProperty(value = "提交状态")
    private String submitStatus;

    /**
     * 提交状态名称
     */
    @ApiModelProperty(value = "提交状态名称")
    private String submitStatusName;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    /**
     * 审核状态名称
     */
    @ApiModelProperty(value = "审核状态名称")
    private String auditStatusName;

    /**
     * 材料文件路径
     */
    @ApiModelProperty(value = "材料文件路径")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 文件大小格式化
     */
    @ApiModelProperty(value = "文件大小格式化")
    private String fileSizeFormatted;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String auditor;

    /**
     * 审核人 ID
     */
    @ApiModelProperty(value = "审核人 ID")
    private Long auditorId;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    /**
     * 终审人
     */
    @ApiModelProperty(value = "终审人")
    private String finalAuditor;

    /**
     * 终审人 ID
     */
    @ApiModelProperty(value = "终审人 ID")
    private Long finalAuditorId;

    /**
     * 终审时间
     */
    @ApiModelProperty(value = "终审时间")
    private Date finalAuditTime;

    /**
     * 终审意见
     */
    @ApiModelProperty(value = "终审意见")
    private String finalAuditOpinion;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}