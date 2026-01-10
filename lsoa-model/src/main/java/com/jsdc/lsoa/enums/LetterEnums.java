package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum LetterEnums {

    UNOPENED_LETTER("1", "未开函"),
    PENDING_APPROVAL("2", "待审批"),
    OPEN_LETTER("3", "已开函"),
    RETURNED("4", "已退回"),
    ;

    LetterEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (LetterEnums myEnum : LetterEnums.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static LetterEnums getValueByDesc(String desc) {
        for (LetterEnums myEnum : LetterEnums.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
