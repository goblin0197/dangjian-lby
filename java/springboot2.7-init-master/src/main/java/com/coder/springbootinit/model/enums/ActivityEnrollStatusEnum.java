package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动参加情况枚举
 *
 */
@Getter
public enum ActivityEnrollStatusEnum {
    /**
     * 已报名
     */
    ENROLLED(1, "已报名"),
    /**
     * 已取消
     */
    CANCELLED(2, "已取消");

    @EnumValue
    private final Integer code;
    private final String description;

    ActivityEnrollStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getCodes() {
        return Arrays.stream(values()).map(item -> item.code).collect(Collectors.toList());
    }

    /**
     * 根据code获取枚举
     * @param code 代码
     * @return 枚举
     */
    public static ActivityEnrollStatusEnum getByCode(Integer code) {
        for (ActivityEnrollStatusEnum activityEnrollStatusEnum : ActivityEnrollStatusEnum.values()) {
            if (activityEnrollStatusEnum.getCode().equals(code)) {
                return activityEnrollStatusEnum;
            }
        }
        return null;
    }
}