package hhplus.lecture.interfaces.api;

import hhplus.lecture.application.LectureFacade;
import hhplus.lecture.application.dto.*;
import hhplus.lecture.interfaces.dto.ResponseDto;
import hhplus.lecture.interfaces.enumcode.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureFacade lectureFacade;


    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody @Valid LectureApplyRequest request) {
        Long response = lectureFacade.apply(request);

        ResponseCode responseCode = response == null ?
                ResponseCode.FAILED_APPLICATION :
                ResponseCode.SUCCESSFUL_APPLICATION;

        HttpStatus status = responseCode.getStatus();

        ResponseDto responseDto = new ResponseDto(status.value(), responseCode.getMsg(), response);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/lectures")
    public ResponseEntity<?> getLectures(
            @RequestParam Long userId,
            @RequestParam String date) {
        List<LectureScheduleResponse> responses = lectureFacade.getAvailableLectureSchedules(new AvailableLectureRequest(userId, date));

        ResponseCode responseCode = responses.isEmpty() ?
                ResponseCode.NO_DATA :
                ResponseCode.AVAILABLE_LECTURE_INFO;

        HttpStatus status = responseCode.getStatus();

        ResponseDto responseDto = new ResponseDto(status.value(), responseCode.getMsg(), responses);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/users/{userId}/lectures")
    public ResponseEntity<?> getUserLectures(@PathVariable Long userId) {
        List<LectureApplyResponse> responses = lectureFacade.getUserApplyInfos(new UserApplyRequest(userId));

        ResponseCode responseCode = responses.isEmpty() ?
                ResponseCode.NO_DATA :
                ResponseCode.COMPLETED_APPLICATION_INFO;

        HttpStatus status = responseCode.getStatus();

        ResponseDto responseDto = new ResponseDto(status.value(), responseCode.getMsg(), responses);

        return new ResponseEntity<>(responseDto, status);
    }

}
