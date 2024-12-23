package hhplus.lecture.infrastructure.repository;

import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface LectureApplyJpaRepository extends JpaRepository<LectureApplyEntity, Long> {

    Optional<LectureApplyEntity> findByUserIdAndScheduleId(@Param("userId") Long userId,
                                                           @Param("scheduleId") Long scheduleId);
}
