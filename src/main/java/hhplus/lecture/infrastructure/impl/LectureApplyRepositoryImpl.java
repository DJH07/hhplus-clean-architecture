package hhplus.lecture.infrastructure.impl;

import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import hhplus.lecture.domain.lectureApply.LectureApplyRepository;
import hhplus.lecture.infrastructure.LectureApplyJpaRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureApplyRepositoryImpl implements LectureApplyRepository {

    private final LectureApplyJpaRepository lectureApplyJpaRepository;

    public Optional<LectureApplyEntity> findByUserIdAndScheduleId(Long userId, Long scheduleId) {
        return lectureApplyJpaRepository.findByUserIdAndScheduleId(userId, scheduleId);
    }

    @Override
    public LectureApplyEntity save(LectureApplyEntity lectureApplyEntity) {
        return lectureApplyJpaRepository.save(lectureApplyEntity);
    }

}