package hhplus.lecture.infrastructure.repository;

import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.infrastructure.dto.LectureApplyProjection;
import hhplus.lecture.infrastructure.dto.LectureScheduleProjection;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureScheduleJpaRepository extends JpaRepository<LectureScheduleEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "5000") // 타임아웃 5초
    })
    @Query("SELECT l FROM LectureScheduleEntity l WHERE l.scheduleId = :id")
    Optional<LectureScheduleEntity> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT new hhplus.lecture.infrastructure.dto.LectureScheduleProjection(" +
            "ls.scheduleId," +
            "ls.lectureId," +
            "l.title," +
            "l.lecturerName," +
            "l.lectureDescription," +
            "ls.startDt," +
            "ls.endDt," +
            "ls.applyCnt" +
            ") " +
            "FROM LectureScheduleEntity ls " +
            "JOIN LectureInfoEntity l ON ls.lectureId = l.lectureId " +
            "WHERE ls.scheduleId NOT IN :scheduleIds " +
            "AND ls.startDt >= :startDt " +
            "AND ls.startDt < :endDt " +
            "AND ls.applyCnt < :maxApplicants " +
            "ORDER BY ls.startDt ASC, ls.lectureId ASC ")
    List<LectureScheduleProjection> findAllExcludingIdsAndByDateRange(
            @Param("scheduleIds") List<Long> scheduleIds,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt,
            @Param("maxApplicants") Integer maxApplicants
    );

    @Query("SELECT new hhplus.lecture.infrastructure.dto.LectureApplyProjection(" +
            "ls.scheduleId," +
            "ls.lectureId," +
            "l.title," +
            "l.lecturerName," +
            "l.lectureDescription," +
            "ls.startDt," +
            "ls.endDt" +
            ") " +
            "FROM LectureScheduleEntity ls " +
            "JOIN LectureInfoEntity l ON ls.lectureId = l.lectureId " +
            "WHERE ls.scheduleId NOT IN :scheduleIds  " +
            "ORDER BY ls.startDt ASC, ls.lectureId ASC")
    List<LectureApplyProjection> findAllExcludingIds(@Param("scheduleIds") List<Long> scheduleIds);
}
