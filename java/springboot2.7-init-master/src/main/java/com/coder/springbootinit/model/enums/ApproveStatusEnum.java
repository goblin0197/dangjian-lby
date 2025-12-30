package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批状态枚举
 *
 */
@Getter
public enum ApproveStatusEnum {
    /**
     * 待审批
     */
    PENDING(1, "待审批"),
    /**
     * 已通过
     */
    APPROVED(2, "已通过"),
    /**
     * 已拒绝
     */
    REJECTED(3, "已拒绝");

    @EnumValue
    private final Integer code;
    private final String description;

    ApproveStatusEnum(Integer code, String description) {
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
    public static ApproveStatusEnum getByCode(Integer code) {
        for (ApproveStatusEnum approveStatusEnum : ApproveStatusEnum.values()) {
            if (approveStatusEnum.getCode().equals(code)) {
                return approveStatusEnum;
            }
        }
        return null;
    }
}