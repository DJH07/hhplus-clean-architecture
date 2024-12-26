package hhplus.lecture.infrastructure.repository;

import hhplus.lecture.domain.lectureSchedule.LectureInfoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LectureInfoJpaRepository extends JpaRepository<LectureInfoEntity, Long> {
}
