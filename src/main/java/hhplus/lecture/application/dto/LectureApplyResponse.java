package hhplus.lecture.application.dto;

public record LectureApplyResponse(
        Long scheduleId,
        Long lectureId,
        String title,
        String lecturerName,
        String lectureDescription,
        String startDt,
        String endDt
) {
}