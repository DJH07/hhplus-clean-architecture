package hhplus.lecture.application.dto;

public record LectureScheduleResponse(
        Long scheduleId,
        Long lectureId,
        String title,
        String lecturerName,
        String lectureDescription,
        String startDt,
        String endDt,
        Integer applyCnt
) {
    public LectureScheduleResponse(Long scheduleId, Long lectureId, String title, String lecturerName, String lectureDescription, String startDt, String endDt, Integer applyCnt) {
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