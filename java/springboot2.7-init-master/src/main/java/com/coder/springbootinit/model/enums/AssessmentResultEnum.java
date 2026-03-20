package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考察结果枚举
 *
 */
@Getter
public enum AssessmentResultEnum {
    /**
     * 不合格
     */
    UNQUALIFIED(0, "不合格"),
    /**
     * 合格
     */
    QUALIFIED(1, "合格"),
    /**
     * 未审核
     */
    PENDING(2, "未审核");

    @EnumValue
    private final Integer value;
    private final String description;

    AssessmentResultEnum(Integer value, String description) {
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
    public static AssessmentResultEnum getByValue(Integer value) {
        for (AssessmentResultEnum resultEnum : AssessmentResultEnum.values()) {
            if (resultEnum.getValue().equals(value)) {
                return resultEnum;
            }
        }
        return null;
    }
}
