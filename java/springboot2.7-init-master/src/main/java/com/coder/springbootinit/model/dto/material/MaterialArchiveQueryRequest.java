package com.coder.springbootinit.model.dto.material;

import com.coder.springbootinit.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 材料归档查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialArchiveQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户姓名（模糊查询）
     */
    @ApiModelProperty(value = "用户姓名（模糊查询）")
    private String userName;

    /**
     * 材料名称（模糊查询）
     */
    @ApiModelProperty(value = "材料名称（模糊查询）")
    private String materialName;

    /**
     * 发展阶段
     */
    @ApiModelProperty(value = "发展阶段")
    private String stage;

    /**
     * 归档开始时间
     */
    @ApiModelProperty(value = "归档开始时间")
    private String archiveStartTime;

    /**
     * 归档结束时间
     */
    @ApiModelProperty(value = "归档结束时间")
    private String archiveEndTime;

    private static final long serialVersionUID = 1L;
}
