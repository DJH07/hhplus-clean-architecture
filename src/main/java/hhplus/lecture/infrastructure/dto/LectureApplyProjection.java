package hhplus.lecture.infrastructure.dto;

import java.time.LocalDateTime;

public record LectureApplyProjection(
        Long scheduleId,
        Long lectureId,
        String title,
        String lecturerName,
        String lectureDescription,
        LocalDateTime startDt,
        LocalDateTime endDt
) {
}