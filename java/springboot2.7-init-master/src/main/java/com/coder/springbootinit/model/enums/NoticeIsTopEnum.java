package com.coder.springbootinit.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告是否置顶枚举
 *
 */
@Getter
public enum NoticeIsTopEnum {
    /**
     * 否
     */
    NO(0, "否"),
    /**
     * 是
     */
    YES(1, "是");

    @EnumValue
    private final Integer code;
    private final String description;

    NoticeIsTopEnum(Integer code, String description) {
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
    public static NoticeIsTopEnum getByCode(Integer code) {
        for (NoticeIsTopEnum noticeIsTopEnum : NoticeIsTopEnum.values()) {
            if (noticeIsTopEnum.getCode().equals(code)) {
                return noticeIsTopEnum;
            }
        }
        return null;
    }
}