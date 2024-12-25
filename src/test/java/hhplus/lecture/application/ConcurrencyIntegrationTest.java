package hhplus.lecture.application;

import hhplus.lecture.application.dto.LectureApplyRequest;
import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyIntegrationTest {

    @Autowired
    private LectureFacade lectureFacade;
    @Autowired
    private LectureInfoJpaRepository lectureInfoJpaRepository;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;
    @Autowired
    private LectureApplyJpaRepository lectureApplyJpaRepository;

    @Test
    @DisplayName("STEP03 - 40명이 동시에 신청 시 30명만 성공하고, 나머지 10명은 실패해야 한다.")
    void testApplyForLectureWithConcurrency() throws InterruptedException {
        // given
        // 특강 정보 생성
        LectureInfoEntity lecture = LectureInfoEntity.create(
                "자바 특강",
                "김자바",
                "김자바의 자바 특강."
        );
        LectureInfoEntity savedLecture = lectureInfoJpaRepository.save(lecture);

        final long lectureId = savedLecture.getLectureId();

        // 특강 일정 정보 생성
        LocalDateTime startDt = LocalDateTime.now().plusDays(1);
        LocalDateTime endDt = startDt.plusHours(1);
        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(
                lectureId,
                startDt,
                endDt);
        LectureScheduleEntity savedLectureSchedule = lectureScheduleJpaRepository.save(lectureSchedule);

        final long scheduleId = savedLectureSchedule.getScheduleId();
        final int maxApplicants = 30;

        // 40명이 동시에 신청을 시도할 스레드들 생성
        int numOfThreads = 40;
        CountDownLatch latch = new CountDownLatch(numOfThreads);

        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>()); // 예외를 수집할 리스트

        for (int i = 0; i < numOfThreads; i++) {
            final long userId = 1L + i;  // 유저 ID는 1부터 40까지 다르게 설정
            executorService.submit(() -> {
                try {
                    // 신청 시도
                    lectureFacade.apply(new LectureApplyRequest(userId, scheduleId));
                } catch (Exception e) {
                    exceptions.add(e);  // 예외 발생 시 수집
                } finally {
                    latch.countDown(); // Latch 카운트 다운
                }
            });
        }

        // when
        latch.await(); // 모든 스레드가 끝날 때까지 대기
        executorService.shutdown(); // ExecutorService 종료

        // then
        // 특강 일정의 신청 인원 수가 최대 신청 인원(30)으로 제한되었는지 확인
        LectureScheduleEntity updatedLectureSchedule =
                lectureScheduleJpaRepository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));

        assertEquals(maxApplicants, updatedLectureSchedule.getApplyCnt());

        // 신청 인원 중에서 40명 중 30명만 신청 성공
        long successfulApplicants = lectureApplyJpaRepository.findAll().size();
        assertEquals(maxApplicants, successfulApplicants);

        // 예외가 발생한 수는 10명(남은 인원)이어야 함
        assertThat(exceptions).hasSize(10);

        // 실패한 신청이 ALREADY_APPLIED 에러인지 확인
        boolean hasDuplicateException = exceptions.stream()
                .anyMatch(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getErrorCode() == LectureErrorCode.MAX_APPLICANTS_REACHED);
        assertTrue(hasDuplicateException, "최대 신청 인원에 도달했습니다");
    }

    @Test
    @DisplayName("STEP04 - 동일한 사용자가 같은 정보를 5번 신청하여 동시성 문제 발생")
    void shouldFail_WhenSameUserAppliesTwiceConcurrently() throws InterruptedException {
        // given
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

        int numOfThreads = 5; // 동일한 사용자의 신청 요청 수
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(numOfThreads);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        // when
        for (int i = 0; i < numOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    lectureFacade.apply(request); // 신청 시도
                } catch (Exception e) {
                    exceptions.add(e); // 발생한 예외를 수집
                } finally {
                    latch.countDown(); // Latch 카운트 다운
                }
            });
        }

        latch.await(); // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();

        // then
        assertThat(exceptions).hasSize(numOfThreads - 1);

        // 중복 신청 예외가 발생했는지 확인
        boolean hasDuplicateException = exceptions.stream()
                .anyMatch(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getErrorCode() == LectureErrorCode.ALREADY_APPLIED);
        assertTrue(hasDuplicateException, "이미 신청한 특강은 신청할 수 없습니다.");

        // 신청 정보가 하나만 저장되었는지 확인
        List<Long> applyIds = lectureApplyJpaRepository.findScheduleIdsByUserId(userId);
        assertThat(applyIds).contains(scheduleId);

        // 특강 신청 인원 수가 1명인지 확인
        LectureScheduleEntity updatedSchedule = lectureScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));
        assertThat(updatedSchedule.getApplyCnt()).isEqualTo(1);
    }


}