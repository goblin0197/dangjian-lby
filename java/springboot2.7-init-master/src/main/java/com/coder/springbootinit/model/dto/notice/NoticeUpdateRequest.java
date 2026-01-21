package com.coder.springbootinit.model.dto.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告更新请求
 *
 */
@Data
public class NoticeUpdateRequest implements Serializable {

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID", required = true)
    private Long id;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String title;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String content;

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
     * 状态：0-草稿，1-已发布
     */
    @ApiModelProperty(value = "状态：0-草稿，1-已发布")
    private Integer status;

    private static final long serialVersionUID = 1L;
}