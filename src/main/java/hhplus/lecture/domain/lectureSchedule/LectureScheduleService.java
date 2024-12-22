package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.util.enumtype.LectureErrorCode;
import hhplus.lecture.util.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static hhplus.lecture.config.Constants.MAX_APPLICANTS;

@Service
@RequiredArgsConstructor
public class LectureScheduleService {
    private final LectureScheduleRepository lectureScheduleRepository;

    /**
     * 신청 인원이 MAX_CNT에 도달했는지 확인하는 메서드
     *
     * @param scheduleId 특강 일정 ID
     */
    public void hasReachedMaxApplicants(Long scheduleId) {
        // 1. 일정 정보 존재 여부 확인
        LectureScheduleEntity lectureSchedule = lectureScheduleRepository.findById(scheduleId);

        // 2. 최대 인원수 도달 여부
        if(lectureSchedule.getApplyCnt() >= MAX_APPLICANTS) {
            throw new BusinessException(LectureErrorCode.MAX_APPLICANTS_REACHED);
        }
    }

    /*
    * 신청인원수 업로드하는 메서드
    */
    public void updateApplyCnt(Long scheduleId) {
        LectureScheduleEntity lectureSchedule = lectureScheduleRepository.findById(scheduleId);
        lectureSchedule.incrementApplyCnt();
    }

}
