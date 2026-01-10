package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum LetterTypeEnums {
    ONE("1", "刑函"),
    TWO("2", "仲裁函"),
    THREE("3", "民函件"),
    FOUR("4", "刑函(法援)"),
    FIVE("5", "破产清算"),
    ;

    LetterTypeEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (LetterTypeEnums myEnum : LetterTypeEnums.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static LetterTypeEnums getValueByDesc(String desc) {
        for (LetterTypeEnums myEnum : LetterTypeEnums.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
