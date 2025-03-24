package com.jeemoo.common.exception.biz;

public class UserLimitExceededException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Integer code = 4001;

    private String message = "当前企业人员已达到最大人数，请联系管理员充值。";

    public UserLimitExceededException() {

    }

    public UserLimitExceededException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public UserLimitExceededException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
