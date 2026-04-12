package com.coder.springbootinit.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 材料归档 VO
 *
 */
@Data
public class MaterialArchiveVO implements Serializable {

    /**
     * 归档 ID
     */
    @ApiModelProperty(value = "归档 ID")
    private Long id;

    /**
     * 提交 ID
     */
    @ApiModelProperty(value = "提交 ID")
    private Long submissionId;

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
     * 归档文件路径
     */
    @ApiModelProperty(value = "归档文件路径")
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
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    /**
     * 归档人
     */
    @ApiModelProperty(value = "归档人")
    private String archiveUser;

    /**
     * 归档人 ID
     */
    @ApiModelProperty(value = "归档人 ID")
    private Long archiveUserId;

    /**
     * 归档时间
     */
    @ApiModelProperty(value = "归档时间")
    private Date archiveTime;

    /**
     * 归档备注
     */
    @ApiModelProperty(value = "归档备注")
    private String archiveRemark;

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