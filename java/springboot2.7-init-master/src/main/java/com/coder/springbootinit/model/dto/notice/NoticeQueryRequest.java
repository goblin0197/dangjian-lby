package com.coder.springbootinit.model.dto.notice;

import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告查询请求
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeQueryRequest extends PageRequest implements Serializable {

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    private Long id;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String title;

    /**
     * 所属党组织ID
     */
    @ApiModelProperty(value = "所属党组织ID,默认全部")
    private Long orgId;

    /**
     * 发布人ID
     */
    @ApiModelProperty(value = "发布人ID")
    private Long publisherId;

    /**
     * 状态:0-草稿/1-已发布
     */
    @ApiModelProperty(value = "状态:0-草稿/1-已发布,默认全部")
    private Integer status;

    /**
     * 是否置顶:0-否,1-是
     */
    @ApiModelProperty(value = "是否置顶:0-否,1-是,默认全部")
    private Integer isTop;

    /**
     * 公告类型
     */
    @ApiModelProperty(value = "公告类型,默认全部")
    private Integer type;


    /**
     * 过期时间开始
     */
    @ApiModelProperty(value = "过期时间开始")
    private Date expireTimeStart;

    /**
     * 过期时间结束
     */
    @ApiModelProperty(value = "过期时间结束")
    private Date expireTimeEnd;
    /**
     * 发布时间开始
     */
    @ApiModelProperty(value = "发布时间开始")
    private Date publishTimeStart;

    /**
     * 发布时间结束
     */
    @ApiModelProperty(value = "发布时间结束")
    private Date publishTimeEnd;

    private static final long serialVersionUID = 1L;
}