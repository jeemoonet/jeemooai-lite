package com.jeemoo.common.exception.biz;

import com.jeemoo.common.exception.ServiceException;

public class IntegralBalanceException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    /**
     * 错误码
     */
    private Integer code = 4001;

    /**
     * 错误提示
     */
    private String message = "当前企业积分不足，请联系管理员充值。";

    public IntegralBalanceException() {
    }

    public IntegralBalanceException(String message) {
        this.message = message;
    }

    public IntegralBalanceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public IntegralBalanceException setMessage(String message) {
        this.message = message;
        return this;
    }
}
