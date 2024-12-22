package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "lecture_apply")
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
    private String startDt;

    @Column(name = "end_dt", nullable = false)
    @Comment("특강 종료일시")
    private String endDt;

    @Column(name = "apply_cnt", nullable = false)
    @Comment("신청인원수")
    private Integer applyCnt;

}
