package hhplus.lecture.application;

import hhplus.lecture.application.dto.AvailableLectureRequest;
import hhplus.lecture.application.dto.LectureApplyRequest;
import hhplus.lecture.application.dto.LectureScheduleResponse;
import hhplus.lecture.domain.dto.LectureScheduleCommand;
import hhplus.lecture.domain.dto.LectureScheduleResult;
import hhplus.lecture.domain.lectureApply.LectureApplyService;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<LectureScheduleResponse> getAvailableLectureSchedules(AvailableLectureRequest request) {

        // 1. 해당 회원이 신청 완료한 특강일정 id 목록 조회
        List<Long> applyScheduleIds = lectureApplyService.getScheduleIdsByUserId(request.userId());

        // 2. 신청하지 않고, 입력받은 날짜 범위 내 특강 정보 조회
        List<LectureScheduleResult> resultList = lectureScheduleService.getLectureResultList(new LectureScheduleCommand(applyScheduleIds, request.date()));

        return resultList.stream()
                .map(result -> new LectureScheduleResponse(
                        result.scheduleId(),
                        result.lectureId(),
                        result.title(),
                        result.lecturerName(),
                        result.lectureDescription(),
                        result.startDt(),
                        result.endDt(),
                        result.applyCnt()))
                .collect(Collectors.toList());
    }
}
