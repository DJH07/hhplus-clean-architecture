package hhplus.lecture.infrastructure.repository;

import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface LectureApplyJpaRepository extends JpaRepository<LectureApplyEntity, Long> {

    Optional<LectureApplyEntity> findByUserIdAndScheduleId(@Param("userId") Long userId,
                                                           @Param("scheduleId") Long scheduleId);

    @Query("SELECT la.scheduleId FROM LectureApplyEntity la WHERE la.userId = :userId")
    List<Long> findScheduleIdsByUserId(@Param("userId") Long userId);
}
