package hhplus.lecture.application.dto;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;

public record UserApplyRequest(
        Long userId
) {
        public UserApplyRequest(Long userId) {
                validateUserId(userId);
                this.userId = userId;
        }

        private void validateUserId(Long userId) {
                if (userId == null || userId <= 0) {
                        throw new BusinessException(LectureErrorCode.INVALID_USER_ID);
                }
        }
}