package hhplus.lecture.infrastructure.impl;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleRepository;
import hhplus.lecture.infrastructure.dto.LectureScheduleProjection;
import hhplus.lecture.infrastructure.repository.LectureScheduleJpaRepository;
import jakarta.persistence.LockTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureScheduleRepositoryImpl implements LectureScheduleRepository {

    private final LectureScheduleJpaRepository lectureScheduleJpaRepository;

    @Override
    public LectureScheduleEntity findByIdWithLock(Long id) {
        try {
            return lectureScheduleJpaRepository.findByIdWithLock(id)
                    .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));
        } catch (LockTimeoutException e) {
            throw new BusinessException(LectureErrorCode.LOCK_TIMEOUT);
        }
    }

    @Override
    public List<LectureScheduleProjection> findAllByUserIdAndDate(List<Long> scheduleIds, LocalDateTime startDt, LocalDateTime endDt, Integer maxApplicants) {
        return lectureScheduleJpaRepository.findAllExcludingIdsAndByDateRange(scheduleIds, startDt, endDt, maxApplicants);
    }

}
