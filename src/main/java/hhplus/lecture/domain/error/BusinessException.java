package hhplus.lecture.domain.error;

import lombok.Getter;

/*
 * 비즈니스 에러 handler
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
