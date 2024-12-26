package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.infrastructure.dto.LectureApplyProjection;
import hhplus.lecture.infrastructure.dto.LectureScheduleProjection;
import jdk.jfr.Description;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureScheduleRepository {

    @Description("특강일정정보 조회")
    LectureScheduleEntity findByIdWithLock(Long id);

    @Description("유저별 신청가능 특강정보 조회")
    List<LectureScheduleProjection> findAllByUserIdAndDate(List<Long> scheduleIds, LocalDateTime startDt, LocalDateTime endDt, Integer maxApplicants);

    @Description("유저별 신청완료 특강정보 조회")
    List<LectureApplyProjection> findAllInIds(List<Long> scheduleIds);
}
