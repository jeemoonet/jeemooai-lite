package com.jeemoo.common.exception.biz;

public class DocumentBaseFullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code = 4001;

    private String message = "知识库容量不足，请联系管理员充值。";

    public DocumentBaseFullException() {

    }

    public DocumentBaseFullException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public DocumentBaseFullException(String message) {
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
