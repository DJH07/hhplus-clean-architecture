package hhplus.lecture.application;

import hhplus.lecture.application.dto.*;
import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import hhplus.lecture.domain.lectureSchedule.LectureInfoEntity;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.infrastructure.repository.LectureApplyJpaRepository;
import hhplus.lecture.infrastructure.repository.LectureInfoJpaRepository;
import hhplus.lecture.infrastructure.repository.LectureScheduleJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class LectureFacadeIntegrationTest {

    @Autowired
    private LectureFacade lectureFacade;
    @Autowired
    private LectureInfoJpaRepository lectureInfoJpaRepository;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;
    @Autowired
    private LectureApplyJpaRepository lectureApplyJpaRepository;


    @Test
    @DisplayName("특강 신청 - 특강 신청이 정상적으로 처리되는 경우")
    void shouldApplySuccessfully_WhenValidScheduleAndUser() {
        // given
        lectureApplyJpaRepository.deleteAll();
        // 특강 정보 생성
        LectureInfoEntity lecture = LectureInfoEntity.create(
                "자바 특강",
                "김자바",
                "김자바의 자바 특강."
        );
        LectureInfoEntity savedLecture = lectureInfoJpaRepository.save(lecture);

        // 특강 일정 정보 생성
        LocalDate lectureDate = LocalDate.now().plusDays(1); // 테스트 날짜 다음날
        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(
                savedLecture.getLectureId(),
                lectureDate.atTime(21, 0),
                lectureDate.atTime(23, 0));
        LectureScheduleEntity savedLectureSchedule = lectureScheduleJpaRepository.save(lectureSchedule);

        final long userId = 1L;
        final long scheduleId = savedLectureSchedule.getScheduleId();
        LectureApplyRequest request = new LectureApplyRequest(userId, scheduleId);

        // when
        Long applyId = lectureFacade.apply(request);

        // then
        assertNotNull(applyId);

        // 신청된 특강의 신청 인원 수 증가 여부 확인
        LectureScheduleEntity updatedSchedule = lectureScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));

        assertEquals(1, updatedSchedule.getApplyCnt()); // 신청 인원이 1 증가했어야 함

        // 신청 정보가 LectureApplyRepository에 저장되었는지 확인
        Optional<LectureApplyEntity> applyEntity = lectureApplyJpaRepository.findById(applyId);
        assertTrue(applyEntity.isPresent()); // 신청 정보가 저장되었는지 확인
        assertEquals(userId, applyEntity.get().getUserId());
        assertEquals(scheduleId, applyEntity.get().getScheduleId());
    }

    @Test
    @DisplayName("신청가능 특강정보리스트 조회 - 사용자가 신청하지 않은 특강 일정이 올바르게 조회되는 경우")
    void shouldReturnAvailableLectureSchedules_WhenUserHasNotApplied() {
        // given
        lectureApplyJpaRepository.deleteAll();
        // 특강 정보 생성
        LectureInfoEntity lecture1 = LectureInfoEntity.create(
                "자바 특강",
                "김자바",
                "김자바의 자바 특강."
        );
        LectureInfoEntity lecture2 = LectureInfoEntity.create(
                "스프링 특강",
                "박스프링",
                "박스프링의 스프링 특강."
        );
        LectureInfoEntity savedLecture1 = lectureInfoJpaRepository.save(lecture1);
        LectureInfoEntity savedLecture2 = lectureInfoJpaRepository.save(lecture2);

        // 특강 일정 정보 생성
        LocalDateTime lectureDate = LocalDateTime.now().plusDays(1); // 테스트 날짜 다음날
        //사용자 신청 특강일정
        LectureScheduleEntity schedule1 = LectureScheduleEntity.create(
                savedLecture1.getLectureId(),
                lectureDate,
                lectureDate.plusHours(2)
        );
        //사용자가 신청하지 않고 조회 가능한 특강 일정
        LectureScheduleEntity schedule2 = LectureScheduleEntity.create(
                savedLecture2.getLectureId(),
                lectureDate,
                lectureDate.plusHours(2)
        );
        // 특강 시작 시간이 지난 경우
        LectureScheduleEntity schedule3 = LectureScheduleEntity.create(
                savedLecture2.getLectureId(),
                lectureDate.minusDays(5),
                lectureDate.minusDays(5).plusHours(2)
        );
        // 특강 정원이 모두 찬 경우
        LectureScheduleEntity schedule4 = LectureScheduleEntity.create(
                savedLecture2.getLectureId(),
                lectureDate,
                lectureDate.plusHours(2)
        );
        schedule4.changeApplyCnt(30);

        LectureScheduleEntity savedSchedule1 = lectureScheduleJpaRepository.save(schedule1);
        LectureScheduleEntity savedSchedule2 = lectureScheduleJpaRepository.save(schedule2);
        lectureScheduleJpaRepository.save(schedule3);
        lectureScheduleJpaRepository.saveAndFlush(schedule4);

        // 사용자가 신청한 특강 일정 저장
        final long userId = 95L;
        LectureApplyEntity appliedSchedule = LectureApplyEntity.create(userId, savedSchedule1.getScheduleId());
        lectureApplyJpaRepository.save(appliedSchedule);

        AvailableLectureRequest request = new AvailableLectureRequest(userId, lectureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // when
        List<LectureScheduleResponse> availableSchedules = lectureFacade.getAvailableLectureSchedules(request);

        // then
        assertNotNull(availableSchedules);
        assertEquals(1, availableSchedules.size()); // 신청하지 않은 일정만 조회되어야 함
        LectureScheduleResponse response = availableSchedules.get(0);

        // 응답 값 검증
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(savedSchedule2.getScheduleId(), response.scheduleId());
        assertEquals(savedLecture2.getLectureId(), response.lectureId());
        assertEquals(savedLecture2.getTitle(), response.title());
        assertEquals(savedLecture2.getLecturerName(), response.lecturerName());
        assertEquals(savedLecture2.getLectureDescription(), response.lectureDescription());
        assertEquals(savedSchedule2.getStartDt().format(formatter), response.startDt());
        assertEquals(savedSchedule2.getEndDt().format(formatter), response.endDt());
        assertEquals(savedSchedule2.getApplyCnt(), response.applyCnt());
    }

    @Test
    @DisplayName("신청완료 리스트 조회 - 사용자가 신청한 특강 일정 정보가 올바르게 조회되는 경우")
    void shouldReturnUserAppliedLectureInfos_WhenUserHasApplied() {
        // given
        lectureApplyJpaRepository.deleteAll();
        // 특강 정보 생성
        LectureInfoEntity lecture1 = LectureInfoEntity.create(
                "자바 특강",
                "김자바",
                "김자바의 자바 특강."
        );
        LectureInfoEntity lecture2 = LectureInfoEntity.create(
                "스프링 특강",
                "박스프링",
                "박스프링의 스프링 특강."
        );
        LectureInfoEntity savedLecture1 = lectureInfoJpaRepository.save(lecture1);
        LectureInfoEntity savedLecture2 = lectureInfoJpaRepository.save(lecture2);

        // 특강 일정 정보 생성
        // 아직 지나지 않은 강의 일정
        LocalDateTime lectureDate = LocalDateTime.now().plusDays(1);
        LectureScheduleEntity schedule1 = LectureScheduleEntity.create(
                savedLecture1.getLectureId(),
                lectureDate,
                lectureDate.plusHours(2)
        );
        // 지난 강의 일정
        lectureDate = LocalDateTime.now().plusDays(3);
        LectureScheduleEntity schedule2 = LectureScheduleEntity.create(
                savedLecture2.getLectureId(),
                lectureDate,
                lectureDate.plusHours(2)
        );
        LectureScheduleEntity savedSchedule1 = lectureScheduleJpaRepository.save(schedule1);
        LectureScheduleEntity savedSchedule2 = lectureScheduleJpaRepository.save(schedule2);

        // 사용자가 신청한 특강 일정 저장
        final long userId = 1L;
        LectureApplyEntity appliedSchedule1 = LectureApplyEntity.create(userId, savedSchedule1.getScheduleId());
        LectureApplyEntity appliedSchedule2 = LectureApplyEntity.create(userId, savedSchedule2.getScheduleId());
        lectureApplyJpaRepository.save(appliedSchedule1);
        lectureApplyJpaRepository.save(appliedSchedule2);

        // when
        List<LectureApplyResponse> appliedLectures = lectureFacade.getUserApplyInfos(new UserApplyRequest(userId));

        // then
        assertNotNull(appliedLectures);
        assertEquals(2, appliedLectures.size()); // 사용자가 신청한 2개의 일정이 조회되어야 함

        // 첫 번째 응답 검증
        LectureApplyResponse response1 = appliedLectures.get(0);
        assertEquals(savedSchedule1.getScheduleId(), response1.scheduleId());
        assertEquals(savedLecture1.getLectureId(), response1.lectureId());
        assertEquals(savedLecture1.getTitle(), response1.title());
        assertEquals(savedLecture1.getLecturerName(), response1.lecturerName());
        assertEquals(savedLecture1.getLectureDescription(), response1.lectureDescription());
        assertEquals(savedSchedule1.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), response1.startDt());
        assertEquals(savedSchedule1.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), response1.endDt());

        // 두 번째 응답 검증
        LectureApplyResponse response2 = appliedLectures.get(1);
        assertEquals(savedSchedule2.getScheduleId(), response2.scheduleId());
        assertEquals(savedLecture2.getLectureId(), response2.lectureId());
        assertEquals(savedLecture2.getTitle(), response2.title());
        assertEquals(savedLecture2.getLecturerName(), response2.lecturerName());
        assertEquals(savedLecture2.getLectureDescription(), response2.lectureDescription());
        assertEquals(savedSchedule2.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), response2.startDt());
        assertEquals(savedSchedule2.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), response2.endDt());
    }

}