package hhplus.lecture.domain.lectureSchedule.dto;

import java.util.List;

public record LectureScheduleCommand(
        List<Long> scheduleIds,
        String date
) {}