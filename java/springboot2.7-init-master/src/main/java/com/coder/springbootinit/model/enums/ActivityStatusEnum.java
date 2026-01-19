package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动状态枚举
 *
 */
@Getter
public enum ActivityStatusEnum {
    /**
     * 待发布
     */
    PENDING(1, "待发布"),
    /**
     * 已发布
     */
    PUBLISHED(2, "已发布"),
    /**
     * 进行中
     */
    IN_PROGRESS(3, "进行中"),
    /**
     * 已结束
     */
    ENDED(4, "已结束");

    @EnumValue
    private final Integer code;
    private final String description;

    ActivityStatusEnum(Integer code, String description) {
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
    public static ActivityStatusEnum getByCode(Integer code) {
        for (ActivityStatusEnum activityStatusEnum : ActivityStatusEnum.values()) {
            if (activityStatusEnum.getCode().equals(code)) {
                return activityStatusEnum;
            }
        }
        return null;
    }
}