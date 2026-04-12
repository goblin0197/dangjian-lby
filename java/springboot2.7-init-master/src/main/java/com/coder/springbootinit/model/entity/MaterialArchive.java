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
 * 材料归档实体
 *
 */
@TableName(value = "material_archive")
@Data
public class MaterialArchive implements Serializable {

    /**
     * 归档 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 提交 ID
     */
    private Long submissionId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 组织层级
     */
    private String orgLevel;

    /**
     * 发展阶段
     */
    private String stage;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 归档文件路径
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 归档人
     */
    private String archiveUser;

    /**
     * 归档人 ID
     */
    private Long archiveUserId;

    /**
     * 归档时间
     */
    private Date archiveTime;

    /**
     * 归档备注
     */
    private String archiveRemark;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}