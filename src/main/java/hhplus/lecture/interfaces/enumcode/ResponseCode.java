package hhplus.lecture.interfaces.enumcode;


import hhplus.lecture.domain.error.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 응답 코드 종류
 */
@RequiredArgsConstructor
public enum ResponseCode implements ErrorCodeEnum {
    SUCCESSFUL_APPLICATION(HttpStatus.OK, "특강 신청이 완료되었습니다."),
    FAILED_APPLICATION(HttpStatus.BAD_REQUEST, "특강 신청에 실패하였습니다."),
    AVAILABLE_LECTURE_INFO(HttpStatus.OK, "신청 가능한 특강 정보를 확인했습니다."),
    COMPLETED_APPLICATION_INFO(HttpStatus.OK, "신청 완료된 특강 정보를 확인했습니다."),
    NO_DATA(HttpStatus.NO_CONTENT, "정보가 존재하지 않습니다.");
    private final HttpStatus status;
    private final String msg;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}