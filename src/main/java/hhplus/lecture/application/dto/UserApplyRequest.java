package hhplus.lecture.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserApplyRequest(
        @NotNull(message = "사용자 ID를 입력해주세요.")
        @Positive(message = "사용자 ID는 반드시 0보다 커야 합니다.")
        Long userId
) {}