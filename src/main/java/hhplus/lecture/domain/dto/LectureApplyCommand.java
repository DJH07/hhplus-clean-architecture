package hhplus.lecture.domain.dto;

import java.util.List;

public record LectureApplyCommand(
        List<Long> scheduleIds
) {}