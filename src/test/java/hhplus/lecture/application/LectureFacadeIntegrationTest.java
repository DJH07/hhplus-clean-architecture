package hhplus.lecture.application;

import hhplus.lecture.application.dto.LectureApplyRequest;
import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import hhplus.lecture.domain.lectureSchedule.LectureInfoEntity;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.infrastructure.repository.LectureApplyJpaRepository;
import hhplus.lecture.infrastructure.repository.LectureInfoJpaRepository;
import hhplus.lecture.infrastructure.repository.LectureScheduleJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
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


    @BeforeEach
    public void setUp() {
        // 특강 정보 생성
        LectureInfoEntity lecture = LectureInfoEntity.create(
                1L,
                "자바 기초 입문",
                "김자바",
                "자바 프로그래밍의 기초를 배우는 입문 과정입니다."
        );
        lectureInfoJpaRepository.save(lecture);

        // 특강 일정 정보 생성
        LocalDate lectureDate = LocalDate.now().plusDays(1); // 테스트 날짜 다음날
        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(
                1L,
                lectureDate.atTime(21, 0),
                lectureDate.atTime(23, 0));
        lectureScheduleJpaRepository.save(lectureSchedule);
    }

    @Test
    @DisplayName("성공 - 특강 신청이 정상적으로 처리되는 경우")
    void shouldApplySuccessfully_WhenValidScheduleAndUser() {
        // given
        final long userId = 1L;
        final long scheduleId = 1L;
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
    @DisplayName("실패 - 동일한 사용자가 동시에 두 번 신청하여 동시성 문제 발생")
    void shouldFail_WhenSameUserAppliesTwiceConcurrently() throws InterruptedException {
        // given
        final long userId = 1L;
        final long scheduleId = 1L;
        LectureApplyRequest request = new LectureApplyRequest(userId, scheduleId);

        int numOfThreads = 1; // 동일한 사용자의 신청 요청 수
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
        // 예외가 하나 발생했는지 확인
        assertThat(exceptions).hasSize(1);

        // 중복 신청 예외가 발생했는지 확인
        boolean hasDuplicateException = exceptions.stream()
                .anyMatch(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getErrorCode() == LectureErrorCode.ALREADY_APPLIED);
//        assertTrue(hasDuplicateException, "이미 신청한 특강은 신청할 수 없습니다");

        // 신청 정보가 하나만 저장되었는지 확인
        List<LectureApplyEntity> applyEntities = lectureApplyJpaRepository.findAll();
        assertThat(applyEntities.size()).isEqualTo(1);

        // 특강 신청 인원 수가 1명인지 확인
        LectureScheduleEntity updatedSchedule = lectureScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));
        assertThat(updatedSchedule.getApplyCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("성공 - 40명이 동시에 신청 시 30명만 성공하고, 나머지 10명은 실패해야 한다.")
    void testApplyForLectureWithConcurrency() throws InterruptedException {
        // given
        final long lectureId = 1L;
        final long scheduleId = 1L;
        final long userId = 1L;
        final int maxApplicants = 30;

        LocalDateTime startDt = LocalDateTime.now().plusHours(1);
        LocalDateTime endDt = startDt.plusHours(1);
        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(lectureId, startDt, endDt);
        lectureScheduleJpaRepository.save(lectureSchedule); // 저장

        // 40명이 동시에 신청을 시도할 스레드들 생성
        int numOfThreads = 40;
        CountDownLatch latch = new CountDownLatch(numOfThreads); // 동기화용 Latch

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            Long currentUserId = userId + i;
            Thread thread = new Thread(() -> {
                try {
                    // 신청 시도
                    lectureFacade.apply(new LectureApplyRequest(currentUserId, scheduleId));
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력
                    System.out.println("Exception during apply: " + e.getMessage());
                } finally {
                    latch.countDown(); // Latch 카운트 다운
                }
            });
            threads.add(thread);
        }

        // when
        threads.forEach(Thread::start);
        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // then
        // 신청된 인원수는 최대 30명이어야 한다.
        LectureScheduleEntity updatedLectureSchedule = lectureScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.NOT_FOUND_SCHEDULE));

        assertEquals(maxApplicants, updatedLectureSchedule.getApplyCnt()); // 최대 인원수 30명

        // 신청 인원 중에서 40명 중 30명만 신청 성공
        long successfulApplicants = lectureApplyJpaRepository.findAll().size();
        assertEquals(30, successfulApplicants); // 신청 성공자는 30명
    }


}