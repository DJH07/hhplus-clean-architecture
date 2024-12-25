package hhplus.lecture.domain;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleEntity;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleRepository;
import hhplus.lecture.domain.lectureSchedule.LectureScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureScheduleServiceUnitTest {

    @Mock
    private LectureScheduleRepository lectureScheduleRepository;

    @InjectMocks
    private LectureScheduleService lectureScheduleService;

    @Test
    @DisplayName("실패 - 정원(최대 인원수)가 다 찼을 시 실패 에러 발생")
    void shouldFailToUpdateApplyCnt_WhenMaxApplicantsReached() {
        // given
        final long scheduleId = 1L;
        final long lectureId = 1L;
        final LocalDateTime startDt = LocalDate.now().atTime(21, 0);
        final LocalDateTime endDt = LocalDate.now().atTime(22, 0);
        final int maxApplicants = 30; // 최대 정원

        // 이미 정원이 찬 상태
        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(lectureId, startDt, endDt);
        lectureSchedule.changeApplyCnt(maxApplicants);

        when(lectureScheduleRepository.findByIdWithLock(scheduleId))
                .thenReturn(lectureSchedule);

        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lectureScheduleService.validateAndUpdateAppyCnt(scheduleId));

        // then
        assertEquals(LectureErrorCode.MAX_APPLICANTS_REACHED, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패 - 특강 시작 시간이 지났을 경우 신청 실패")
    void shouldFailToUpdateApplyCnt_WhenLectureStartTimeHasPassed() {
        // given
        final long scheduleId = 2L;
        final long lectureId = 2L;
        final LocalDateTime startDt = LocalDateTime.now().minusHours(1); // 과거 시간
        final LocalDateTime endDt = LocalDate.now().atTime(22, 0);
        final int applyCnt = 10;

        LectureScheduleEntity lectureSchedule = LectureScheduleEntity.create(lectureId, startDt, endDt);
        lectureSchedule.changeApplyCnt(applyCnt);

        when(lectureScheduleRepository.findByIdWithLock(scheduleId))
                .thenReturn(lectureSchedule);

        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lectureScheduleService.validateAndUpdateAppyCnt(scheduleId));

        // then
        assertEquals(LectureErrorCode.APPLICATION_PERIOD_CLOSED, exception.getErrorCode());
    }

}
