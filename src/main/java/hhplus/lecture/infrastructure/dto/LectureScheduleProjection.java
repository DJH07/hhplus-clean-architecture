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
    public LectureScheduleProjection(Long scheduleId, Long lectureId, String title, String lecturerName, String lectureDescription, LocalDateTime startDt, LocalDateTime endDt, Integer applyCnt) {
        this.scheduleId = scheduleId;
        this.lectureId = lectureId;
        this.title = title;
        this.lecturerName = lecturerName;
        this.lectureDescription = lectureDescription;
        this.startDt = startDt;
        this.endDt = endDt;
        this.applyCnt = applyCnt;
    }
}