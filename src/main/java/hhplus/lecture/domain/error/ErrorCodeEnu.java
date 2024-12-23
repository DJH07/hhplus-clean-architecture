package hhplus.lecture.domain.error;

import org.springframework.http.HttpStatus;

public interface ErrorCodeEnu {
    HttpStatus getStatus();
    String getMsg();
}
