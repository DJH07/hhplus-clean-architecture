package hhplus.lecture.interfaces.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    /*
    * TODO: 특강 신청 API
    */
    @PostMapping("/apply")
    public ResponseEntity<?> apply() {
        return null;
    }

    /*
    * TODO: 특강 신청 가능 목록 조회 API
    */
    @GetMapping("/getLectures")
    public ResponseEntity<?> getLectures() {
        return null;
    }

    /*
    * TODO: 특강 신청 완료 목록 조회 API
     */
    @PostMapping("/getUserLectures")
    public ResponseEntity<?> getUserLectures() {
        return null;
    }

}
