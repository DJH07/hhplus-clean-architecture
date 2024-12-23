package hhplus.lecture.application;

import hhplus.lecture.domain.lectureApply.LectureApplyService;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureScheduleService lectureScheduleService;
    private final LectureApplyService lectureApplyService;

    @Transactional
    public Long apply(Long userId, Long scheduleId) {
        // 0. userId로 유저 정보가 있는지 확인한다. (그러나 이번에는 user 테이블이 없으니 생략한다.)

        // 1. 일정정보 조회해서 선착순이 끝나지 않았는지 확인 및 신청 인원수 update
        lectureScheduleService.validateAndUpdateAppyCnt(scheduleId);

        // 2. 특강신청 정보 조회해서 이미 신청했는지 확인 후 저장
        lectureApplyService.insertApply(userId, scheduleId);

        return null;
    }
}
