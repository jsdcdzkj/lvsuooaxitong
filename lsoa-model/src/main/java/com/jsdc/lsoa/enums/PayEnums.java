package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum PayEnums {
    PAY("1", "已支付"),
    NOT_PAY("2", "未支付"),
    ;

    PayEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;

    public static String getDescByValue(String value) {
        for (PayEnums myEnum : PayEnums.values()) {
            if (Objects.equals(myEnum.getValue(), value)) {
                return myEnum.getDesc();
            }
        }
        return null;
    }

    public static PayEnums getValueByDesc(String desc) {
        for (PayEnums myEnum : PayEnums.values()) {
            if (myEnum.getDesc().equals(desc)) {
                return myEnum;
            }
        }
        return null;
    }
}
