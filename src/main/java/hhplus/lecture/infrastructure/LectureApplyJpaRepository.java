package hhplus.lecture.infrastructure;

import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LectureApplyJpaRepository extends JpaRepository<LectureApplyEntity, Long> {
}
