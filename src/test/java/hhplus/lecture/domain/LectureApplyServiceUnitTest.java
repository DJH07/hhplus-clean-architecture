package hhplus.lecture.domain;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureApply.LectureApplyEntity;
import hhplus.lecture.domain.lectureApply.LectureApplyRepository;
import hhplus.lecture.domain.lectureApply.LectureApplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureApplyServiceUnitTest {
    @Mock
    private LectureApplyRepository lectureApplyRepository;

    @InjectMocks
    private LectureApplyService lectureApplyService;

    @Test
    @DisplayName("실패 - 동일한 시간대의 동일한 특강에 중복 신청할 경우 실패")
    void shouldFailToInsertApply_WhenDuplicateApplicationBySameUser() {
        // given
        final Long userId = 1L;
        final Long scheduleId = 1L;

        // 중복 신청이 이미 존재한다고 가정
        when(lectureApplyRepository.findByUserIdAndScheduleId(userId, scheduleId))
                .thenReturn(Optional.of(LectureApplyEntity.create(userId, scheduleId)));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lectureApplyService.insertApply(userId, scheduleId));

        // then
        assertEquals(LectureErrorCode.ALREADY_APPLIED, exception.getErrorCode());
        verify(lectureApplyRepository, never()).saveAndGetId(any(LectureApplyEntity.class)); // 저장 시도되지 않았는지 확인
    }

}
