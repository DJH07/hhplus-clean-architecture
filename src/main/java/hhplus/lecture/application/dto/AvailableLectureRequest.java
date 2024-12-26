package hhplus.lecture.application.dto;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record AvailableLectureRequest(
        Long userId,
        String date
) {
        public AvailableLectureRequest(Long userId, String date) {
                validateUserId(userId);
                this.userId = userId;
                this.date = parseDate(date);
        }

        private void validateUserId(Long userId) {
                if (userId == null || userId <= 0) {
                        throw new BusinessException(LectureErrorCode.INVALID_USER_ID);
                }
        }

        private String parseDate(String date) {
                if (date == null || date.isBlank()) {
                        throw new BusinessException(LectureErrorCode.INVALID_DATE);
                }
                try {
                        LocalDate.parse(date);
                        return date;
                } catch (DateTimeParseException e) {
                        throw new BusinessException(LectureErrorCode.INVALID_DATE_FORMAT);
                }
        }}