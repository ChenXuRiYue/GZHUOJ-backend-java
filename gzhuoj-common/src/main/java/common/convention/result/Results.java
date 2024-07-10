package common.convention.result;

import common.convention.errorcode.BaseErrorCode;
import common.exception.AbstractException;

import java.util.Optional;

public final class Results {
    /**
     * 构造成功响应
     */
    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 带返回数据的成功响应
     */
    public static <T> Result<T> success(T data){
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    /**
     * 构建服务器失败响应
     */
    public static Result<Void> failure() {
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    public static Result<Void> failure(AbstractException abstractexception){
        String errorCode = Optional.ofNullable(abstractexception.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractexception.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    public static Result<Void> failure(String errorCode, String errorMessage){
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }
}
