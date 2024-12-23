package hhplus.lecture.domain.lectureApply;

import hhplus.lecture.domain.util.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "lecture_apply")
public class LectureApplyEntity extends AuditingFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id", nullable = false)
    @Comment("신청 정보 id")
    private Long applyId;

    @Column(name = "user_id", nullable = false)
    @Comment("사용자 id")
    private Long userId;

    @Column(name = "schedule_id", nullable = false)
    @Comment("특강 일정 정보 아이디")
    private Long scheduleId;

    public LectureApplyEntity(Long userId, Long scheduleId) {
        this.userId = userId;
        this.scheduleId = scheduleId;
    }
}
