package hhplus.lecture.domain.lectureSchedule;

import hhplus.lecture.domain.error.BusinessException;
import hhplus.lecture.domain.error.LectureErrorCode;
import hhplus.lecture.domain.lectureSchedule.dto.LectureScheduleCommand;
import hhplus.lecture.domain.lectureSchedule.dto.LectureScheduleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hhplus.lecture.domain.util.Constants.MAX_APPLICANTS;

@Service
@RequiredArgsConstructor
public class LectureScheduleService {
    private final LectureScheduleRepository lectureScheduleRepository;

    /**
     * 신청 인원이 MAX_CNT에 도달했는지 확인하고 신청 인원수 증가하는 메서드
     *
     * @param scheduleId 특강 일정 ID
     */
    public void validateAndUpdateAppyCnt(Long scheduleId) {
        // 1. 일정 정보 존재 여부 확인 (존재하지 않을 시 NOT_FOUND 오류)
        LectureScheduleEntity lectureSchedule = lectureScheduleRepository.findByIdWithLock(scheduleId);

        // 2. 최대 인원수 도달 여부 확인
        if(lectureSchedule.getApplyCnt() >= MAX_APPLICANTS) {
            throw new BusinessException(LectureErrorCode.MAX_APPLICANTS_REACHED);
        }

        // 3. 특강 시작 시간과 같거나 지났을 경우, 실패 처리
        if(!lectureSchedule.getStartDt().isAfter(LocalDateTime.now())) {
            throw new BusinessException(LectureErrorCode.APPLICATION_PERIOD_CLOSED);
        }

        // 4. 신청 인원수 증가
        lectureSchedule.changeApplyCnt(lectureSchedule.getApplyCnt() + 1);
    }

    /**
     * 날짜와 일정정보 id 리스트를 입력받아, id 리스트를 제외한 해당 날짜 강의 정보 조회
     *
     */
    public List<LectureScheduleResult> getLectureResultList(LectureScheduleCommand command) {
        // 1. 조회 조건에 필요한 LocalDateTime 값으로 변환
        LocalDate date = LocalDate.parse(command.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startDt = date.atStartOfDay();
        LocalDateTime endDt = date.atStartOfDay().plusDays(1);

        // 2. 강의시작시간이 이미 지났다면 검색 조회 대상 제외
        // (검색조건 시작시간이 지금시간보다 이전이면, startDt를 현재 시간으로)
        LocalDateTime currentDt = LocalDateTime.now();
        if(startDt.isBefore(currentDt)) {
            startDt = currentDt.plusNanos(1);
        }

        // 3. LectureScheduleProjection의 날짜 형식을 String으로 변환할 formatter
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 4. scheduleIds에 포함되지 않고, 검색 날짜 내인 강의 정보 조회
        return lectureScheduleRepository.findAllByUserIdAndDate(command.scheduleIds(), startDt, endDt)
                .stream()
                .map(dto -> new LectureScheduleResult(
                        dto.scheduleId(),
                        dto.lectureId(),
                        dto.title(),
                        dto.lecturerName(),
                        dto.lectureDescription(),
                        dto.startDt().format(formatter),
                        dto.endDt().format(formatter),
                        dto.applyCnt()
                ))
                .toList();
    }

}
