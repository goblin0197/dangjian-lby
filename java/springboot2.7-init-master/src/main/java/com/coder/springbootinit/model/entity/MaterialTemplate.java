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
 * 材料模板实体
 *
 */
@TableName(value = "material_template")
@Data
public class MaterialTemplate implements Serializable {

    /**
     * 模板 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 所属发展阶段
     */
    private String stage;

    /**
     * 材料类型
     */
    private String type;

    /**
     * 模板状态：enable-启用/disable-停用
     */
    private String status;

    /**
     * 模板文件路径
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 上传用户 ID
     */
    private Long uploadUserId;

    /**
     * 上传用户姓名
     */
    private String uploadUserName;

    /**
     * 模板说明
     */
    private String remark;

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