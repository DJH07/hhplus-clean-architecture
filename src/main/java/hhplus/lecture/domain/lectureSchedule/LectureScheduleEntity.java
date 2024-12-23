package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "lecture_schedule")
public class LectureScheduleEntity extends AuditingFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    @Comment("특강 일정 정보 아이디")
    private Long scheduleId;

    @Column(name = "lecture_id", nullable = false)
    @Comment("특강 아이디")
    private Long lectureId;

    @Column(name = "start_dt", nullable = false)
    @Comment("특강 시작일시")
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    @Comment("특강 종료일시")
    private LocalDateTime endDt;

    @Column(name = "apply_cnt", nullable = false)
    @Comment("신청인원수")
    private Integer applyCnt;

    @Version
    @Column(name = "version", nullable = false)
    @Comment("버전")
    private Integer version;

    // 신청인원수 증가 메서드
    public void incrementApplyCnt() {
        this.applyCnt++;
    }
}
