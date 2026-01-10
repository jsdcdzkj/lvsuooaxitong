package com.jsdc.lsoa.vo;

import lombok.Data;

/**
 * @ClassName ResultInfo 统一返回类
 * @Author xujian
 * @Date 2021/12/30 17:21
 * @Version 1.0
 */
@Data
public class ResultInfo<T> {

    private Integer code;

    private String msg;

    private T data;


    /**
     *  成功
     */
    public static ResultInfo<Void> success(){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        resultInfo.setCode(0);
        resultInfo.setMsg("成功");
        return resultInfo;
    }


    /**
     * 成功返回
     * @param o 返回对象
     */
    public static <T> ResultInfo<T> success(T o){
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.setCode(0);
        resultInfo.setMsg("成功");
        resultInfo.setData(o);
        return resultInfo;
    }


    /**
     * 失败返回
     * @param msg 错误信息
     */
    public static ResultInfo<Void> error(String msg){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        resultInfo.setCode(-1);
        resultInfo.setMsg(msg);
        return resultInfo;
    }


    /**
     * 失败返回
     * @param msg 错误信息
     */
    public static ResultInfo<Void> error(Integer code, String msg){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        resultInfo.setCode(code);
        resultInfo.setMsg(msg);
        return resultInfo;
    }
}
