package com.coder.springbootinit.model.dto.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 材料提交通求
 *
 */
@Data
public class MaterialSubmissionSubmitRequest implements Serializable {

    /**
     * 用户 ID
     */
    @NotNull(message = "用户 ID 不能为空")
    @ApiModelProperty(value = "用户 ID", required = true)
    private Long userId;

    /**
     * 用户姓名
     */
    @NotBlank(message = "用户姓名不能为空")
    @ApiModelProperty(value = "用户姓名", required = true)
    private String userName;

    /**
     * 组织层级
     */
    @NotBlank(message = "组织层级不能为空")
    @ApiModelProperty(value = "组织层级", required = true)
    private String orgLevel;

    /**
     * 发展阶段
     */
    @NotBlank(message = "发展阶段不能为空")
    @ApiModelProperty(value = "发展阶段", required = true)
    private String stage;

    /**
     * 材料名称
     */
    @NotBlank(message = "材料名称不能为空")
    @ApiModelProperty(value = "材料名称", required = true)
    private String materialName;

    /**
     * 材料文件路径
     */
    @NotBlank(message = "材料文件路径不能为空")
    @ApiModelProperty(value = "材料文件路径", required = true)
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}
