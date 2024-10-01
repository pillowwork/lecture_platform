package org.lecture.platform.init

import org.lecture.platform.domain.apply.dto.ApplyRequestDto
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LecturePlatformApplicationRunner(
  private val applyRepository: ApplyRepository,
  private val lectureRepository: LectureRepository,
): ApplicationRunner {

  companion object {
    val logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun run(args: ApplicationArguments?) {
    logger.info("LECTURE_PLATFORM application init")

    /** 초기 실행 후, 샘플 데이터 입력 */
    // 강연
    val start = LocalDateTime.now()
    val end = start.plusHours(2)

    val lecture1 = LectureEntity.makeEntity(LectureRegisterDto(
      speaker = "김창옥",
      roomName = "강연장1",
      capacity = 10,
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = start,
      endTime = end,
    ))
    val lecture2 = LectureEntity.makeEntity(LectureRegisterDto(
      speaker = "김미경",
      roomName = "강연장1",
      capacity = 10,
      title = "인생명언",
      description = "오늘 하루 괜찮았나요",
      startTime = start.plusHours(2),
      endTime = end.plusHours(4),
    ))
    val lecture3 = LectureEntity.makeEntity(LectureRegisterDto(
      speaker = "법륜스님",
      roomName = "강연장1",
      capacity = 10,
      title = "즉문즉설",
      description = "생각보다 간단한 행복해지는 방법",
      startTime = start.plusHours(4),
      endTime = end.plusHours(6),
    ))
    val lecture4 = LectureEntity.makeEntity(LectureRegisterDto(
      speaker = "Josh Long",
      roomName = "강연장1",
      capacity = 10,
      title = "New Spring Feature",
      description = "Spring AI",
      startTime = start.plusHours(6),
      endTime = end.plusHours(8),
    ))
    lectureRepository.saveAllAndFlush(
      arrayListOf(lecture1, lecture2, lecture3, lecture4)
    )
    logger.info("LECTURE_PLATFORM application init :: INSERT sample LECTURE data")

    // 강연 신청
    val apply1 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0001", lectureId = 1), lecture1)
    val apply2 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0002", lectureId = 1), lecture1)
    val apply3 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0003", lectureId = 1), lecture1)
    val apply4 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0004", lectureId = 1), lecture1)
    val apply5 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0005", lectureId = 1), lecture1)
    val apply6 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0006", lectureId = 1), lecture1)
    val apply7 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0007", lectureId = 1), lecture1)
    val apply8 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0008", lectureId = 1), lecture1)
    val apply9 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0009", lectureId = 1), lecture1)
    val apply10 = ApplyEntity.makeEntity(ApplyRequestDto(employeeId = "A0010", lectureId = 1), lecture1)
    applyRepository.saveAllAndFlush(
      arrayListOf(
        apply1, apply2, apply3, apply4, apply5, apply6, apply7, apply8, apply9, apply10
      )
    )
    logger.info("LECTURE_PLATFORM application init :: INSERT sample APPLY data")

  }

}