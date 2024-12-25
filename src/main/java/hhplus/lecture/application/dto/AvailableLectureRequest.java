package hhplus.lecture.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record AvailableLectureRequest(
        @NotNull(message = "사용자 ID를 입력해주세요.")
        @Positive(message = "사용자 ID는 반드시 0보다 커야 합니다.")
        Long userId,

        @NotBlank(message = "날짜를 입력해주세요.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd이어야 합니다.")
        String date
) {}