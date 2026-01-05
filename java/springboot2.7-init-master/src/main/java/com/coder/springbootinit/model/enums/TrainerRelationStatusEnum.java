package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 培养状态
 *
 */
@Getter
public enum TrainerRelationStatusEnum {
    /**
     * 进行中
     */
    TRAINING(1, "进行中"),
    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),
    /**
     * 已终止
     */
    TERMINATED(3, "已终止");

    @EnumValue
    private final Integer value;
    private final String description;

    TrainerRelationStatusEnum(Integer value, String description) {
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
    public static TrainerRelationStatusEnum getByValue(Integer value) {
        for (TrainerRelationStatusEnum approveStatusEnum : TrainerRelationStatusEnum.values()) {
            if (approveStatusEnum.getValue().equals(value)) {
                return approveStatusEnum;
            }
        }
        return null;
    }
}