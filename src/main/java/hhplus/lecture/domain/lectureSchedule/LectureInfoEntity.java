package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.domain.util.AuditingFields;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "lecture_info")
public class LectureInfoEntity extends AuditingFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id", nullable = false)
    @Comment("특강 id")
    private Long lectureId;

    @Column(name = "title", nullable = false, length = 255)
    @Comment("특강명")
    private String title;

    @Column(name = "lecturer_name", nullable = false, length = 255)
    @Comment("강연자명")
    private String lecturerName;

    @Column(name = "lecture_description", columnDefinition = "TEXT")
    @Comment("특강 설명")
    private String lectureDescription;

    public static LectureInfoEntity create(String title, String lecturerName, String lectureDescription) {
        LectureInfoEntity entity = new LectureInfoEntity();
        entity.title = title;
        entity.lecturerName = lecturerName;
        entity.lectureDescription = lectureDescription;
        return entity;
    }
}
