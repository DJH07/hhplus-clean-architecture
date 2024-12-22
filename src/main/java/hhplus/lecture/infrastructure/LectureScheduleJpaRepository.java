package hhplus.lecture.infrastructure;

import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LectureScheduleJpaRepository extends JpaRepository<LectureScheduleEntity, Long> {
}
