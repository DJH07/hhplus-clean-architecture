package hhplus.lecture.domain.error;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 오류 코드 종류
 */
@RequiredArgsConstructor
public enum LectureErrorCode implements ErrorCodeEnum {
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "사용자 ID가 유효하지 않습니다."),
    INVALID_SCHEDULE_ID(HttpStatus.BAD_REQUEST, "강의일정정보 ID가 유효하지 않습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "날짜를 입력해주세요."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식은 yyyy-MM-dd이어야 합니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "해당 강의일정정보가 존재하지 않습니다."),
    MAX_APPLICANTS_REACHED(HttpStatus.BAD_REQUEST, "최대 신청 인원에 도달했습니다."),
    APPLICATION_PERIOD_CLOSED(HttpStatus.BAD_REQUEST, "특강 신청 시간이 지났습니다."),
    ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 신청한 특강은 신청할 수 없습니다."),
    LOCK_TIMEOUT(HttpStatus.BAD_REQUEST, "현재 처리 중인 요청이 많아 신청이 지연되고 있습니다.");
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