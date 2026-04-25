package com.coder.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告VO
 *
 */
@Data
public class NoticeVO implements Serializable {

    /**
     * 公告ID
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 所属党组织ID
     */
    private Long orgId;

    /**
     * 所属党组织名称
     */
    private String orgName;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 是否置顶:0-否,1-是
     */
    private Integer isTop;

    /**
     * 状态:0-草稿/1-已发布/2-已撤回
     */
    private Integer status;

    /**
     * 公告类型
     */
    private Integer type;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 是否置顶名称
     */
    private String isTopName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}