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
 * 公告分享实体
 *
 */
@TableName(value = "announcement_share")
@Data
public class AnnouncementShare implements Serializable {

    /**
     * 分享 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告 ID
     */
    private Long announcementId;

    /**
     * 分享链接
     */
    private String shareUrl;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 分享用户 ID
     */
    private Long shareUserId;

    /**
     * 分享用户姓名
     */
    private String shareUserName;

    /**
     * 分享时间
     */
    private Date shareTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 查看次数
     */
    private Integer viewCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}