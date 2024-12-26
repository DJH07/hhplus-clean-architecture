package hhplus.lecture.domain.dto;

public record LectureApplyResult(
        Long scheduleId,
        Long lectureId,
        String title,
        String lecturerName,
        String lectureDescription,
        String startDt,
        String endDt
) {
}