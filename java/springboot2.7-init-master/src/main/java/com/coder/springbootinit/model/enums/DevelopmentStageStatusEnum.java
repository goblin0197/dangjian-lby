package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发展阶段状态枚举
 *
 */
@Getter
public enum DevelopmentStageStatusEnum {
    /**
     * 进行中
     */
    IN_PROGRESS(0, "进行中"),
    /**
     * 已完成
     */
    COMPLETED(1, "已完成"),
    /**
     * 已终止
     */
    TERMINATED(2, "已终止");

    @EnumValue
    private final Integer value;
    private final String description;

    DevelopmentStageStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据value获取枚举
     * @param value 代码
     * @return 枚举
     */
    public static DevelopmentStageStatusEnum getByValue(Integer value) {
        for (DevelopmentStageStatusEnum statusEnum : DevelopmentStageStatusEnum.values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
