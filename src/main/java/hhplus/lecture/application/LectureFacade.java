package hhplus.lecture.application;

import hhplus.lecture.application.dto.LectureApplyRequest;
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
    public Long apply(LectureApplyRequest request) {

        // 1. 일정정보 조회해서 선착순이 끝나지 않았는지 확인 및 신청 인원수 update
        lectureScheduleService.validateAndUpdateAppyCnt(request.scheduleId());

        // 2. 특강신청 정보 조회해서 이미 신청했는지 확인 후 저장
        return lectureApplyService.insertApply(request.userId(), request.scheduleId());

    }
}
