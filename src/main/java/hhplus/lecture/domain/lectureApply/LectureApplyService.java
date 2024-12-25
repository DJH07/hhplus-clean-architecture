package hhplus.lecture.domain.lectureApply;


import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureApplyService {
    private final LectureApplyRepository lectureApplyRepository;

    /**
     * 특정 유저가 이미 신청했는지 확인하는 메서드
     *
     * @param userId 사용자 ID
     * @param scheduleId 특강 일정 ID
     */
    public Long insertApply(Long userId, Long scheduleId) {
        // 1. 특정 유저가 이미 신청했는지 확인한다
        if (lectureApplyRepository.findByUserIdAndScheduleId(userId, scheduleId).isPresent()) {
            throw new BusinessException(LectureErrorCode.ALREADY_APPLIED);
        }

        // 2. 신청 정보를 저장한다.
        LectureApplyEntity lectureApply = new LectureApplyEntity(userId, scheduleId);
        return lectureApplyRepository.saveAndGetId(lectureApply);
    }
}
