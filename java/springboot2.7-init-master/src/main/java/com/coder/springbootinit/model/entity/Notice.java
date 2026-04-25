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
 * 公告
 *
*/
@TableName(value = "notice")
@Data
public class Notice implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;

    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 公告类型
     */
    private Integer type;

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

    /**
     * 首次发布时间
     */
    private Date firstPublishTime;

    /**
     * 阅读量
     */
    private Integer readCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}