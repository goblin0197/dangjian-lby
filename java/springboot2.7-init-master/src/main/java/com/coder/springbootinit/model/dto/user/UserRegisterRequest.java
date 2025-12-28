package com.coder.springbootinit.model.dto.user;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册请求体
 *
*/
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @ApiModelProperty(value = "账号", required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true)
    private String userPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    private String checkPassword;
}
