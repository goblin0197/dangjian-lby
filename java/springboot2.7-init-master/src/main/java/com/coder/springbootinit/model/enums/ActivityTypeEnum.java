package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动类型枚举
 *
 */
@Getter
public enum ActivityTypeEnum {
    /**
     * 会议
     */
    MEETING(1, "会议"),
    /**
     * 志愿活动
     */
    VOLUNTEER(2, "志愿活动"),
    /**
     * 学习
     */
    STUDY(3, "学习"),
    /**
     * 其他
     */
    OTHER(4, "其他");

    @EnumValue
    private final Integer code;
    private final String description;

    ActivityTypeEnum(Integer code, String description) {
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
    public static ActivityTypeEnum getByCode(Integer code) {
        for (ActivityTypeEnum activityTypeEnum : ActivityTypeEnum.values()) {
            if (activityTypeEnum.getCode().equals(code)) {
                return activityTypeEnum;
            }
        }
        return null;
    }
}