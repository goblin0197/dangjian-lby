package com.coder.springbootinit.model.dto.file;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传请求
 *
*/
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    @ApiModelProperty(value = "业务:material-材料,template-模板", required = true)
    private String biz;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "党组织ID,业务为template时必填")
    private Long orgId;

    private static final long serialVersionUID = 1L;
}