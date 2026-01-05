package com.coder.springbootinit.model.dto.file;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传请求
 * 目前暂时好像没用
 *
*/
@Data
public class DownloadFileRequest implements Serializable {

    /**
     * 业务
     */
    @ApiModelProperty(value = "业务:material-材料,template-模板",required = true)
    private String biz;
    
    @ApiModelProperty(value = "用户ID,材料业务必填")
    private long userId;

    @ApiModelProperty(value = "文件ID",required = true)
    private String fileId;

    private static final long serialVersionUID = 1L;
}