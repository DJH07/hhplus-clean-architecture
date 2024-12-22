package hhplus.lecture.infrastructure.impl;

import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleRepository;
import hhplus.lecture.infrastructure.LectureScheduleJpaRepository;
import hhplus.lecture.util.enumtype.LectureErrorCode;
import hhplus.lecture.util.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureScheduleRepositoryImpl implements LectureScheduleRepository {

    private final LectureScheduleJpaRepository lectureScheduleJpaRepository;

    @Override
    public LectureScheduleEntity findById(Long id) {
        return lectureScheduleJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));
    }
}
