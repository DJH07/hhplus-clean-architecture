package hhplus.lecture.application;

import hhplus.lecture.application.dto.AvailableLectureRequest;
import hhplus.lecture.application.dto.UserApplyRequest;
import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LectureFacadeUnitTest {

    @Test
    @DisplayName("실패 - 유효하지 않은 userId로 요청 시 BusinessException 발생")
    void shouldFail_WhenUserIdIsInvalid() {
        // given
        final Long invalidUserId = -1L;
        final String validDate = "2024-12-31";

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new AvailableLectureRequest(invalidUserId, validDate);
        });

        // then
        assertEquals(LectureErrorCode.INVALID_USER_ID, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패 - 유효하지 않은 date로 요청 시 BusinessException 발생")
    void shouldFail_WhenDateIsInvalid() {
        // given
        final Long validUserId = 1L;
        final String invalidDate = "invalid-date";

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new AvailableLectureRequest(validUserId, invalidDate);
        });

        // then
        assertEquals(LectureErrorCode.INVALID_DATE_FORMAT, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패 - userId가 null인 경우 BusinessException 발생")
    void shouldFail_WhenUserIdIsNull() {
        // given
        final Long invalidUserId = null;

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new UserApplyRequest(invalidUserId); // 예외 발생
        });

        // then
        assertEquals(LectureErrorCode.INVALID_USER_ID, exception.getErrorCode());
    }
}