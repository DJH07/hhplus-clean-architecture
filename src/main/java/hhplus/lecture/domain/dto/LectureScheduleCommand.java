package hhplus.lecture.domain.dto;

import java.util.List;

public record LectureScheduleCommand(
        List<Long> scheduleIds,
        String date
) {}