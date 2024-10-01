package org.lecture.platform.back.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.entity.RoomEntity
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class BackOfficeServiceTest {

  private lateinit var applyRepository: ApplyRepository
  private lateinit var lectureRepository: LectureRepository
  private lateinit var roomRepository: RoomRepository

  private lateinit var backOfficeService: BackOfficeService

  private lateinit var room: RoomEntity
  private lateinit var now: LocalDateTime

  @BeforeEach
  fun setUp() {
    applyRepository = mockk<ApplyRepository>()
    lectureRepository = mockk<LectureRepository>()
    roomRepository = mockk<RoomRepository>()
    backOfficeService = BackOfficeService(applyRepository, lectureRepository, roomRepository)

    room = RoomEntity(
      id = 1,
      name = "강의실 A",
      capacity = 200
    )
    now = LocalDateTime.now()
  }

  @Test
  fun listLectureTest() {
    val pageable: Pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"))
    val entity1 = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    val entity2 = LectureEntity(
      id = 2,
      speaker = "김미경",
      title = "인생명언",
      description = "오늘 하루 괜찮았나요",
      startTime = now.plusHours(2),
      endTime = now.plusHours(4),
      room = room
    )
    val entity3 = LectureEntity(
      id = 3,
      speaker = "법륜스님",
      title = "즉문즉설",
      description = "생각보다 간단한 행복해지는 방법",
      startTime = now.plusHours(4),
      endTime = now.plusHours(6),
      room = room
    )
    val list = arrayListOf(entity3, entity2, entity1)
    val page = PageImpl(list, pageable, list.size.toLong())

    val dto2 = entity2.toDto()
    val dto3 = entity3.toDto()

    every { lectureRepository.findAll(pageable) } returns page

    // when
    val savedPage = backOfficeService.listLecture(pageable)

    // then
    assertNotNull(savedPage)
    assertEquals(savedPage.totalElements ,3)
    assertEquals(savedPage.totalPages ,2)
    assertEquals(savedPage.number ,0)

    assertEquals(savedPage.content[0].id, dto3.id)
    assertEquals(savedPage.content[0].speaker, dto3.speaker)
    assertEquals(savedPage.content[0].title, dto3.title)
    assertEquals(savedPage.content[0].description, dto3.description)
    assertEquals(savedPage.content[0].startTime, dto3.startTime)
    assertEquals(savedPage.content[0].endTime, dto3.endTime)
    assertEquals(savedPage.content[0].roomId, dto3.roomId)

    assertEquals(savedPage.content[1].id, dto2.id)
    assertEquals(savedPage.content[1].speaker, dto2.speaker)
    assertEquals(savedPage.content[1].title, dto2.title)
    assertEquals(savedPage.content[1].description, dto2.description)
    assertEquals(savedPage.content[1].startTime, dto2.startTime)
    assertEquals(savedPage.content[1].endTime, dto2.endTime)
    assertEquals(savedPage.content[1].roomId, dto2.roomId)
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
    var lectureList = emptyList<LectureEntity>()
    every { roomRepository.findByIdOrNull(room.id) } returns room
    every { lectureRepository.findLectureByRoomIdAndTimeRange(room.id, request.startTime, request.endTime) } returns lectureList
    every { lectureRepository.save(any()) } returns entity

    // when
    var savedDto = backOfficeService.registerLecture(request)

    // then
    verify { roomRepository.findByIdOrNull(room.id) }
    verify { lectureRepository.findLectureByRoomIdAndTimeRange(room.id, request.startTime, request.endTime) }
    verify { lectureRepository.save(any()) }

    assertNotNull(savedDto)
    assertEquals(savedDto.speaker, "김창옥")
    assertEquals(savedDto.title, "포프리쇼")
    assertEquals(savedDto.description, "행복한 순간을 기억해주세요")
  }

  @Test
  fun registerLectureTest_fail_ROOM_NO_INFO() {
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

    assertEquals(exception.message, ErrorEnum.ROOM_NO_INFO.name)
  }

  @Test
  fun registerLectureTest_fail_LECTURE_TIME_OVERLAP() {
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
    var lectureList = arrayListOf(entity)
    every { roomRepository.findByIdOrNull(room.id) } returns room
    every { lectureRepository.findLectureByRoomIdAndTimeRange(room.id, request.startTime, request.endTime) } returns lectureList
    every { lectureRepository.save(any()) } returns entity

    // when
    var exception = assertThrows<Exception> {
      backOfficeService.registerLecture(request)
    }

    // then
    verify { roomRepository.findByIdOrNull(request.roomId) }

    assertEquals(exception.message, ErrorEnum.LECTURE_TIME_OVERLAP.name)
  }

  @Test
  fun memberListLecture_success() {
    val pageable: Pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))
    val lecture = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    val entity1 = ApplyEntity(id = 1, employeeId = "12345", lecture = lecture)
    val entity2 = ApplyEntity(id = 2, employeeId = "23456", lecture = lecture)
    val entity3 = ApplyEntity(id = 3, employeeId = "34567", lecture = lecture)
    val entity4 = ApplyEntity(id = 4, employeeId = "45678", lecture = lecture)
    val entity5 = ApplyEntity(id = 5, employeeId = "56789", lecture = lecture)
    val list = arrayListOf(entity5, entity4, entity3, entity2, entity1)
    val page = PageImpl(list, pageable, list.size.toLong())

    val dto3 = entity3.toDto()
    val dto4 = entity4.toDto()
    val dto5 = entity5.toDto()

    every { lectureRepository.findByIdOrNull(lecture.id) } returns lecture
    every { applyRepository.findByLectureId(lecture.id, pageable) } returns page

    // when
    val savedPage = backOfficeService.memberListLecture(lecture.id, pageable)

    // then
    assertNotNull(savedPage)
    assertEquals(savedPage.totalElements ,5)
    assertEquals(savedPage.totalPages ,2)
    assertEquals(savedPage.number ,0)

    assertEquals(savedPage.content[0].id, dto5.id)
    assertEquals(savedPage.content[0].employeeId, dto5.employeeId)

    assertEquals(savedPage.content[1].id, dto4.id)
    assertEquals(savedPage.content[1].employeeId, dto4.employeeId)

    assertEquals(savedPage.content[2].id, dto3.id)
    assertEquals(savedPage.content[2].employeeId, dto3.employeeId)
  }

  @Test
  fun memberListLecture_fail_LECTURE_NO_INFO() {
    val pageable: Pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))
    val lecture = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )

    every { lectureRepository.findByIdOrNull(lecture.id) } returns null

    // when
    var exception = assertThrows<Exception> {
      backOfficeService.memberListLecture(lecture.id, pageable)
    }

    // then
    verify { lectureRepository.findByIdOrNull(lecture.id) }

    assertEquals(exception.message, ErrorEnum.LECTURE_NO_INFO.name)
  }

}