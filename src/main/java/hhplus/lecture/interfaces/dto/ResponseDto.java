package hhplus.lecture.interfaces.dto;

public record ResponseDto(
        Integer code,
        String msg,
        Object data
) {
}
