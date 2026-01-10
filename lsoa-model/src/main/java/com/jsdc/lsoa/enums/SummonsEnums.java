package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum SummonsEnums {
    NOT_INVOICED("1", "未开函"),
    PENDING_APPROVAL("2", "待审批"),
    INVOICE_HAS_BEEN_ISSUED("3", "已开函"),
    RETURNED("4", "已退回"),
    ;

    SummonsEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (LogEnums myEnum : LogEnums.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static LogEnums getValueByDesc(String desc) {
        for (LogEnums myEnum : LogEnums.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
