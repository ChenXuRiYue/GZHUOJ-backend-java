package common.exception;

import common.convention.errorcode.BaseErrorCode;
import common.convention.errorcode.IErrorCode;

import java.util.Optional;

import static common.convention.errorcode.BaseErrorCode.USER_ACCOUNT_VERIFY_ERROR;

// 校验失败抛出的异常类定义
public class UnauthorizedException extends AbstractException {

    public UnauthorizedException(String message) {
        this(message, null, BaseErrorCode.USER_ACCOUNT_VERIFY_ERROR);
    }

    public UnauthorizedException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public UnauthorizedException(String message, Throwable throwable){
        this(message, throwable, null);
    }

    public UnauthorizedException(String message, Throwable throwable, IErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }

    @Override
    public String toString() {
        return "UnauthorizedException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
