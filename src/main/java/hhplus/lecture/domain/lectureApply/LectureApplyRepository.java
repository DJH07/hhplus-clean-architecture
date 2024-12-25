package hhplus.lecture.domain.lectureApply;

import jdk.jfr.Description;

import java.util.List;
import java.util.Optional;

public interface LectureApplyRepository {

    @Description("유저 신청 정보 조회")
    Optional<LectureApplyEntity> findByUserIdAndScheduleId(Long userId, Long scheduleId);

    @Description("저장 메서드")
    Long saveAndGetId(LectureApplyEntity lectureApplyEntity);

    @Description("유저가 신청한 일정 정보 id 리스트 조회")
    List<Long> findScheduleIdsByUserId(Long userId);
}
