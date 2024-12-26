package hhplus.lecture.infrastructure.dto;

import java.time.LocalDateTime;

public record LectureScheduleProjection(
        Long scheduleId,
        Long lectureId,
        String title,
        String lecturerName,
        String lectureDescription,
        LocalDateTime startDt,
        LocalDateTime endDt,
        Integer applyCnt
) {
}