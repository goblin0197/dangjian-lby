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
 * 公告附件实体
 *
 */
@TableName(value = "announcement_attachment")
@Data
public class AnnouncementAttachment implements Serializable {

    /**
     * 附件 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公告 ID
     */
    private Long announcementId;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 附件路径
     */
    private String url;

    /**
     * 附件大小
     */
    private String size;

    /**
     * 附件类型
     */
    private String type;

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