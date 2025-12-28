package com.coder.springbootinit.model.dto.user;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录请求
 *
*/
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @ApiModelProperty(value = "账号", required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true)
    private String userPassword;
}
