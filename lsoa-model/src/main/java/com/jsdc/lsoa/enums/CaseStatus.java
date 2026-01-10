package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;
@Getter
public enum CaseStatus {
    UNDER_WAY("1", "进行中"),
    FINISHED("2", "已完结"),
    DISCONTINUED("3", "已中止（解除委托或撤销）"),
    ;

    CaseStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (CaseStatus myEnum : CaseStatus.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static CaseStatus getValueByDesc(String desc) {
        for (CaseStatus myEnum : CaseStatus.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
