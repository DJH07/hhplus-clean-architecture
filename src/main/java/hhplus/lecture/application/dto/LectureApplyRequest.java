package hhplus.lecture.application.dto;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;

public record LectureApplyRequest(
        Long userId,

        Long scheduleId
) {
        public LectureApplyRequest(Long userId, Long scheduleId) {
                if (userId == null || userId <= 0) {
                        throw new BusinessException(LectureErrorCode.INVALID_USER_ID);
                }
                if (scheduleId == null || scheduleId <= 0) {
                        throw new BusinessException(LectureErrorCode.INVALID_SCHEDULE_ID);
                }
                this.userId = userId;
                this.scheduleId = scheduleId;
        }
}