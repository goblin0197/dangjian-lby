package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发展阶段操作类型枚举
 *
 */
@Getter
public enum DevelopmentStageOperationEnum {
    /**
     * 创建
     */
    CREATE("创建", "创建"),
    /**
     * 更新
     */
    UPDATE("更新", "更新"),
    /**
     * 提交审核
     */
    SUBMIT_AUDIT("提交审核", "提交审核"),
    /**
     * 审核
     */
    AUDIT("审核", "审核"),
    /**
     * 删除
     */
    DELETE("删除", "删除");

    @EnumValue
    private final String value;
    private final String description;

    DevelopmentStageOperationEnum(String value, String description) {
        this.value = value;
        this.description = description;
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
     * 根据value获取枚举
     * @param value 代码
     * @return 枚举
     */
    public static DevelopmentStageOperationEnum getByValue(String value) {
        for (DevelopmentStageOperationEnum operationEnum : DevelopmentStageOperationEnum.values()) {
            if (operationEnum.getValue().equals(value)) {
                return operationEnum;
            }
        }
        return null;
    }
}
