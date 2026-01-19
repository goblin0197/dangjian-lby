package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动签到状态枚举
 *
 */
@Getter
public enum ActivitySignStatusEnum {
    /**
     * 未签到
     */
    NOT_SIGNED(0, "未签到"),
    /**
     * 已签到
     */
    SIGNED(1, "已签到");

    @EnumValue
    private final Integer code;
    private final String description;

    ActivitySignStatusEnum(Integer code, String description) {
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
    public static ActivitySignStatusEnum getByCode(Integer code) {
        for (ActivitySignStatusEnum activitySignStatusEnum : ActivitySignStatusEnum.values()) {
            if (activitySignStatusEnum.getCode().equals(code)) {
                return activitySignStatusEnum;
            }
        }
        return null;
    }
}