package com.coder.springbootinit.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtils {
    /**
     * 加密盐值
     */
    private static final String SALT = "coder";
    /**
     * 加密密码
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password){
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }
}
