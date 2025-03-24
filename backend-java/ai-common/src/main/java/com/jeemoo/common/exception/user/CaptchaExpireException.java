package com.jeemoo.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author ai
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire");
    }
}
