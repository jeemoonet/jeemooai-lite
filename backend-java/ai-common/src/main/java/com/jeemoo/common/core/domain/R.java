package com.jeemoo.common.core.domain;

import com.jeemoo.common.constant.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int _FAIL = 500;

    private int code;

    private String msg;

    private T data;

    private T otherData;

    private T schema;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, SUCCESS, msg);
    }
    public static <T> R<T> ok(String msg, T data,T otherData) {
        return restResult(data,otherData, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, _FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, _FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, _FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, _FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg) {
        return restResult(null, HttpStatus.WARN, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }

    public static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
    public static <T> R<T> restResult(T data,T otherData,int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setOtherData(otherData);
        r.setMsg(msg);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
