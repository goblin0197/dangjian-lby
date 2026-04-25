package com.coder.springbootinit.model.dto.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告添加请求
 *
 */
@Data
public class NoticeAddRequest implements Serializable {

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题", required = true)
    private String title;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容", required = true)
    private String content;

    /**
     * 所属党组织ID(null表示系统公告)
     */
    @ApiModelProperty(value = "所属党组织ID(null表示系统公告)")
    private Long orgId;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    /**
     * 是否置顶:0-否,1-是
     */
    @ApiModelProperty(value = "是否置顶:0-否,1-是")
    private Integer isTop;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 状态:0-草稿/1-已发布
     */
    @ApiModelProperty(value = "状态:0-草稿/1-已发布")
    private Integer status;

    /**
     * 公告类型
     */
    @ApiModelProperty(value = "公告类型")
    private Integer type;

    private static final long serialVersionUID = 1L;
}