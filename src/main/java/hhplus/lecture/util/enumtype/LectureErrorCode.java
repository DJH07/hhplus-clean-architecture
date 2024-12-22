package hhplus.lecture.util.enumtype;


import hhplus.lecture.util.error.ErrorCodeEnu;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 오류 코드 종류
 */
@RequiredArgsConstructor
public enum LectureErrorCode implements ErrorCodeEnu {
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "사용자 ID가 유효하지 않습니다."),
    INVALID_LECTURE_ID(HttpStatus.BAD_REQUEST, "강의정보 ID가 유효하지 않습니다."),
    INVALID_SCHEDULE_ID(HttpStatus.BAD_REQUEST, "강의일정정보 ID가 유효하지 않습니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "해당 강의일정정보 존재하지 않습니다."),
    MAX_APPLICANTS_REACHED(HttpStatus.BAD_REQUEST, "최대 신청 인원에 도달했습니다."),
    ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 신청한 특강은 신청할 수 없습니다.");
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