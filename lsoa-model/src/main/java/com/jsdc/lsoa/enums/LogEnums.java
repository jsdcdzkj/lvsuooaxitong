package com.jsdc.lsoa.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum LogEnums {
    LOG_LOGIN("1", "登录"),
    LOG_UP_PASSWORD("1-1", "修改密码"),
    LOG_UPDATE("1-2", "更新个人信息"),
//    LOG_SIGN_NO("1-3", "注销用户"),
//    LOG_WX_LOGIN("1-4", "微信登陆"),
    LOG_EXIT("2", "退出"),

    ;

    LogEnums(String value, String desc) {
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
