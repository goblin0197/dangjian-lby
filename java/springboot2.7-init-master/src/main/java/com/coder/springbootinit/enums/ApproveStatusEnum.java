package com.coder.springbootinit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

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