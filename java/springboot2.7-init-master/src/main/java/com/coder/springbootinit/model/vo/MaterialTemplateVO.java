package com.coder.springbootinit.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 材料模板 VO
 *
 */
@Data
public class MaterialTemplateVO implements Serializable {

    /**
     * 模板 ID
     */
    @ApiModelProperty(value = "模板 ID")
    private Long id;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;

    /**
     * 所属发展阶段
     */
    @ApiModelProperty(value = "所属发展阶段")
    private String stage;

    /**
     * 材料类型
     */
    @ApiModelProperty(value = "材料类型")
    private String type;

    /**
     * 模板状态：enable-启用/disable-停用
     */
    @ApiModelProperty(value = "模板状态")
    private String status;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称")
    private String statusName;

    /**
     * 模板文件路径
     */
    @ApiModelProperty(value = "模板文件路径")
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
     * 上传用户 ID
     */
    @ApiModelProperty(value = "上传用户 ID")
    private Long uploadUserId;

    /**
     * 上传用户姓名
     */
    @ApiModelProperty(value = "上传用户姓名")
    private String uploadUserName;

    /**
     * 模板说明
     */
    @ApiModelProperty(value = "模板说明")
    private String remark;

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