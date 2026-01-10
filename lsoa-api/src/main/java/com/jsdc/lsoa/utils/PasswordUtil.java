package com.jsdc.lsoa.utils;

import com.jsdc.lsoa.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PasswordUtil {


    public static void validate(String password) {
        if (StringUtils.isBlank(password)) {
            throw new CustomException("密码不能为空");
        }
        if (password.length() < 8 || password.length() > 16) {
            throw new CustomException("密码长度应在8~16位");
        }
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        if (!upperCasePattern.matcher(password).find()) {
            throw new CustomException("密码至少包含一个大写字母");
        }
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        if (!lowerCasePattern.matcher(password).find()) {
            throw new CustomException("密码至少包含一个小写字母");
        }
        Pattern digitPattern = Pattern.compile("[0-9]");
        if (!digitPattern.matcher(password).find()) {
            throw new CustomException("密码至少包含一个数字");
        }
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        if (!specialCharPattern.matcher(password).find()) {
            throw new CustomException("密码至少包含一个特殊字符");
        }
    }

}
