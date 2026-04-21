package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告状态枚举
 *
 */
@Getter
public enum NoticeStatusEnum {
    /**
     * 草稿
     */
    DRAFT(0, "草稿"),
    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),
    /**
     * 已下架
     */
    OFFLINE(2, "已下架");

    @EnumValue
    private final Integer code;
    private final String description;

    NoticeStatusEnum(Integer code, String description) {
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
    public static NoticeStatusEnum getByCode(Integer code) {
        for (NoticeStatusEnum noticeStatusEnum : NoticeStatusEnum.values()) {
            if (noticeStatusEnum.getCode().equals(code)) {
                return noticeStatusEnum;
            }
        }
        return null;
    }
}