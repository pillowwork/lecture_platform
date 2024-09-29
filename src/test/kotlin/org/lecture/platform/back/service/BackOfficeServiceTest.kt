package org.lecture.platform.back.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.entity.RoomEntity
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class BackOfficeServiceTest {

  private lateinit var lectureRepository: LectureRepository
  private lateinit var roomRepository: RoomRepository

  private lateinit var backOfficeService: BackOfficeService

  private lateinit var room: RoomEntity
  private lateinit var now: LocalDateTime

  @BeforeEach
  fun setUp() {
    lectureRepository = mockk<LectureRepository>()
    roomRepository = mockk<RoomRepository>()
    backOfficeService = BackOfficeService(lectureRepository, roomRepository)

    room = RoomEntity(
      id = 1,
      name = "강의실 A",
      capacity = 200
    )
    now = LocalDateTime.now()
  }

  @Test
  fun registerLectureTest_success() {
    // set
    var request = LectureRegisterDto(
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      roomId = room.id,
    )
    var entity = LectureEntity.makeEntity(request, room)
    every { roomRepository.findByIdOrNull(room.id) } returns room
    every { lectureRepository.save(any()) } returns entity

    // when
    var savedDto = backOfficeService.registerLecture(request)

    // then
    verify { roomRepository.findByIdOrNull(room.id) }
    verify { lectureRepository.save(any()) }

    assertNotNull(savedDto)
    assertEquals(savedDto.speaker, "김창옥")
    assertEquals(savedDto.title, "포프리쇼")
    assertEquals(savedDto.description, "행복한 순간을 기억해주세요")
  }

  @Test
  fun registerLectureTest_fail_no_room_info() {
    // set
    var request = LectureRegisterDto(
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      roomId = 999,
    )
    every { roomRepository.findByIdOrNull(request.roomId) } returns null

    // when
    var exception = assertThrows<Exception> {
      backOfficeService.registerLecture(request)
    }

    // then
    verify { roomRepository.findByIdOrNull(request.roomId) }

    assertEquals(exception.message, ErrorEnum.ROOM_NO_INFO.message)
  }

}