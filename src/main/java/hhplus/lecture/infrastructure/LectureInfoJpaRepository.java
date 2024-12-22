package hhplus.lecture.infrastructure;

import hhplus.lecture.domain.lectureInfo.LectureInfoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LectureInfoJpaRepository extends JpaRepository<LectureInfoEntity, Long> {
}
