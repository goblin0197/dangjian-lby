package com.coder.springbootinit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 组织关系转移类型枚举
 *
 */
@Getter
public enum TransferTypeEnum {
    /**
     * 转入
     */
    TRANSFER_IN(1, "转入"),
    /**
     * 转出
     */
    TRANSFER_OUT(2, "转出"),
    /**
     * 内部调整
     */
    INTERNAL_ADJUST(3, "内部调整");

    @EnumValue
    private final Integer code;
    private final String description;

    TransferTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     * @param code 代码
     * @return 枚举
     */
    public static TransferTypeEnum getByCode(Integer code) {
        for (TransferTypeEnum transferTypeEnum : TransferTypeEnum.values()) {
            if (transferTypeEnum.getCode().equals(code)) {
                return transferTypeEnum;
            }
        }
        return null;
    }
}