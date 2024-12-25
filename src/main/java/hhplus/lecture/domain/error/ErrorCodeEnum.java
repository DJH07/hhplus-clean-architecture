package hhplus.lecture.domain.error;

import org.springframework.http.HttpStatus;

public interface ErrorCodeEnum {
    HttpStatus getStatus();
    String getMsg();
}
