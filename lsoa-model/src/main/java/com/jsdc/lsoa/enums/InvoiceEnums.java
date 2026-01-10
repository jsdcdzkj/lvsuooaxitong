package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;
@Getter
public enum InvoiceEnums {
    NOT_INVOICED("1", "未开票"),
    PENDING_APPROVAL("2", "待审批"),
    INVOICE_HAS_BEEN_ISSUED("3", "已开票"),
    RETURNED("4", "已退回"),
    ;

    InvoiceEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (InvoiceEnums myEnum : InvoiceEnums.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static InvoiceEnums getValueByDesc(String desc) {
        for (InvoiceEnums myEnum : InvoiceEnums.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
