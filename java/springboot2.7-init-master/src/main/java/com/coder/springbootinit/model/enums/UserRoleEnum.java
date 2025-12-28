package com.coder.springbootinit.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 用户角色枚举
 *
*/
public enum UserRoleEnum {

    SUPER_ADMIN("超级管理员", "super_admin"),
    ORG_ADMIN("组织管理员", "org_admin"),
    PARTY_MEMBER("党员", "party_member"),
    ACTIVIST_DEVELOPMENT("积极分子/发展对象", "activist_development");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据值列表获取枚举数组
     *
     * @param values
     * @return
     */
    public static UserRoleEnum[] getEnumsByValues(String[] values) {
        if (ObjectUtils.isEmpty(values)) {
            return new UserRoleEnum[0];
        }
        return Arrays.stream(values).map(UserRoleEnum::getEnumByValue).toArray(UserRoleEnum[]::new);
    }

    /**
     * 获取角色value和text列表
     * @return
     */
    public static Map<String, String> getRoleMap() {
        return Arrays.stream(values()).collect(Collectors.toMap(item -> item.getValue(), item -> item.getText()));
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
