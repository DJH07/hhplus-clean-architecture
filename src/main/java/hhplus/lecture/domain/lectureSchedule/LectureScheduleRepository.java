package hhplus.lecture.domain.lectureSchedule;

import jdk.jfr.Description;

public interface LectureScheduleRepository {

    @Description("특강 일정 정보 조회")
    LectureScheduleEntity findById(Long id);

}
