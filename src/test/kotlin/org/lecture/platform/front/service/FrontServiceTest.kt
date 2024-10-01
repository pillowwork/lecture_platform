package org.lecture.platform.front.service

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.dto.ApplyRequestDto
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureDto
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
class FrontServiceTest {

  private lateinit var applyRepository: ApplyRepository
  private lateinit var lectureRepository: LectureRepository
  private lateinit var roomRepository: RoomRepository

  private lateinit var frontService: FrontService

  private lateinit var room: RoomEntity
  private lateinit var now: LocalDateTime

  private lateinit var lecture1: LectureEntity
  private lateinit var lecture2: LectureEntity
  private lateinit var lecture3: LectureEntity

  private lateinit var lectureDto1: LectureDto
  private lateinit var lectureDto2: LectureDto
  private lateinit var lectureDto3: LectureDto

  @BeforeEach
  fun setUp() {
    applyRepository = mockk<ApplyRepository>()
    lectureRepository = mockk<LectureRepository>()
    roomRepository = mockk<RoomRepository>()
    frontService = FrontService(applyRepository, lectureRepository)

    room = RoomEntity(
      id = 1,
      name = "강의실 A",
      capacity = 200
    )
    now = LocalDateTime.now()
    lecture1 = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now.plusHours(1),
      endTime = now.plusHours(2),
      room = room
    )
    lecture2 = LectureEntity(
      id = 2,
      speaker = "김미경",
      title = "인생명언",
      description = "오늘 하루 괜찮았나요",
      startTime = now.plusHours(3),
      endTime = now.plusHours(4),
      room = room
    )
    lecture3 = LectureEntity(
      id = 3,
      speaker = "법륜스님",
      title = "즉문즉설",
      description = "생각보다 간단한 행복해지는 방법",
      startTime = now.plusHours(5),
      endTime = now.plusHours(6),
      room = room
    )
    lectureDto1 = lecture1.toDto()
    lectureDto2 = lecture2.toDto()
    lectureDto3 = lecture3.toDto()
  }

  @Test
  fun listLectureTest() {
    val pageable: Pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"))

    val list = arrayListOf(lecture3, lecture2, lecture1)
    val page = PageImpl(list, pageable, list.size.toLong())

    every { lectureRepository.findApplicableLectureList(pageable) } returns page

    // when
    val savedPage = frontService.listLecture(pageable)

    // then
    assertNotNull(savedPage)
    assertEquals(savedPage.totalElements ,3)
    assertEquals(savedPage.totalPages ,2)
    assertEquals(savedPage.number ,0)

    assertEquals(savedPage.content[0].id, lectureDto3.id)
    assertEquals(savedPage.content[0].speaker, lectureDto3.speaker)
    assertEquals(savedPage.content[0].title, lectureDto3.title)
    assertEquals(savedPage.content[0].description, lectureDto3.description)
    assertEquals(savedPage.content[0].startTime, lectureDto3.startTime)
    assertEquals(savedPage.content[0].endTime, lectureDto3.endTime)
    assertEquals(savedPage.content[0].roomId, lectureDto3.roomId)

    assertEquals(savedPage.content[1].id, lectureDto2.id)
    assertEquals(savedPage.content[1].speaker, lectureDto2.speaker)
    assertEquals(savedPage.content[1].title, lectureDto2.title)
    assertEquals(savedPage.content[1].description, lectureDto2.description)
    assertEquals(savedPage.content[1].startTime, lectureDto2.startTime)
    assertEquals(savedPage.content[1].endTime, lectureDto2.endTime)
    assertEquals(savedPage.content[1].roomId, lectureDto2.roomId)
  }

  @Test
  fun applyLectureTest_success() {
    val request = ApplyRequestDto(
      employeeId = "ABCDE",
      lectureId = 1
    )
    val entity = ApplyEntity.makeEntity(request, lecture1)
    val list = emptyList<ApplyEntity>()

    every { lectureRepository.findByIdOrNull(request.lectureId) } returns lecture1
    every { applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId) } returns list
    every { applyRepository.save(any()) } returns entity

    // when
    val savedDto = frontService.applyLecture(request)

    // then
    verify { lectureRepository.findByIdOrNull(request.lectureId) }
    verify { applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId) }
    verify { applyRepository.save(any()) }

    assertNotNull(savedDto)
    assertEquals(savedDto.employeeId, request.employeeId)
    assertEquals(savedDto.lecture.id, request.lectureId)
  }

  @Test
  fun applyLectureTest_fail_LECTURE_NO_INFO() {
    val request = ApplyRequestDto(
      employeeId = "ABCDE",
      lectureId = 1
    )

    every { lectureRepository.findByIdOrNull(request.lectureId) } returns null

    // when
    val exception = assertThrows<Exception> {
      frontService.applyLecture(request)
    }

    // then
    verify { lectureRepository.findByIdOrNull(request.lectureId) }

    assertEquals(exception.message, ErrorEnum.LECTURE_NO_INFO.name)
  }

  @Test
  fun applyLectureTest_fail_APPLY_ALREADY_APPLIED() {
    val request = ApplyRequestDto(
      employeeId = "ABCDE",
      lectureId = 1
    )
    val entity = ApplyEntity.makeEntity(request, lecture1)
    val list = arrayListOf(entity)

    every { lectureRepository.findByIdOrNull(request.lectureId) } returns lecture1
    every { applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId) } returns list

    // when
    val exception = assertThrows<Exception> {
      frontService.applyLecture(request)
    }

    // then
    verify { lectureRepository.findByIdOrNull(request.lectureId) }
    verify { applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId) }

    assertEquals(exception.message, ErrorEnum.APPLY_ALREADY_APPLIED.name)
  }

  @Test
  fun applyListLectureTest() {
    val pageable: Pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"))
    val employeeId = "ABCDE"
    val request1 = ApplyRequestDto(
      employeeId = employeeId,
      lectureId = 1
    )
    val request2 = ApplyRequestDto(
      employeeId = employeeId,
      lectureId = 2
    )
    val request3 = ApplyRequestDto(
      employeeId = employeeId,
      lectureId = 3
    )
    val entity1 = ApplyEntity.makeEntity(request1, lecture1)
    val entity2 = ApplyEntity.makeEntity(request2, lecture2)
    val entity3 = ApplyEntity.makeEntity(request3, lecture3)

    val list = arrayListOf(entity3, entity2, entity1)
    val page = PageImpl(list, pageable, list.size.toLong())

    every { applyRepository.findByEmployeeId(employeeId, pageable) } returns page

    // when
    val savedPage = frontService.applyListLecture(employeeId, pageable)

    // then
    assertNotNull(savedPage)
    assertEquals(savedPage.totalElements ,3)
    assertEquals(savedPage.totalPages ,2)
    assertEquals(savedPage.number ,0)

    assertEquals(savedPage.content[0].employeeId, request3.employeeId)
    assertEquals(savedPage.content[0].lecture.id, request3.lectureId)

    assertEquals(savedPage.content[1].employeeId, request2.employeeId)
    assertEquals(savedPage.content[1].lecture.id, request2.lectureId)
  }

  @Test
  fun applyCancelLectureTest_success() {
    val entity = ApplyEntity(
      id = 1,
      employeeId = "ABCDE",
      createTime = now,
      lecture = lecture1
    )

    every { applyRepository.findByIdOrNull(1) } returns entity
    every { applyRepository.delete(entity) } just runs

    // when
    frontService.applyCancelLecture(1)

    // then
    verify { applyRepository.findByIdOrNull(1) }
    verify { applyRepository.delete(entity) }
  }

  @Test
  fun applyCancelLectureTest_fail_APPLY_NO_INFO() {
    every { applyRepository.findByIdOrNull(1) } returns null

    // when
    val exception = assertThrows<Exception> {
      frontService.applyCancelLecture(1)
    }

    // then
    verify { applyRepository.findByIdOrNull(1) }

    assertEquals(exception.message, ErrorEnum.APPLY_NO_INFO.name)
  }

  @Test
  fun popularLectureTest() {
    val pageable: Pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"))

    val list = arrayListOf(lecture3, lecture2, lecture1)
    val page = PageImpl(list, pageable, list.size.toLong())

    every { lectureRepository.findApplyByPopular(pageable) } returns page

    // when
    val savedPage = frontService.popularLecture(pageable)

    // then
    assertNotNull(savedPage)
    assertEquals(savedPage.totalElements ,3)
    assertEquals(savedPage.totalPages ,2)
    assertEquals(savedPage.number ,0)

    assertEquals(savedPage.content[0].id, lectureDto3.id)
    assertEquals(savedPage.content[0].speaker, lectureDto3.speaker)
    assertEquals(savedPage.content[0].title, lectureDto3.title)
    assertEquals(savedPage.content[0].description, lectureDto3.description)
    assertEquals(savedPage.content[0].startTime, lectureDto3.startTime)
    assertEquals(savedPage.content[0].endTime, lectureDto3.endTime)
    assertEquals(savedPage.content[0].roomId, lectureDto3.roomId)

    assertEquals(savedPage.content[1].id, lectureDto2.id)
    assertEquals(savedPage.content[1].speaker, lectureDto2.speaker)
    assertEquals(savedPage.content[1].title, lectureDto2.title)
    assertEquals(savedPage.content[1].description, lectureDto2.description)
    assertEquals(savedPage.content[1].startTime, lectureDto2.startTime)
    assertEquals(savedPage.content[1].endTime, lectureDto2.endTime)
    assertEquals(savedPage.content[1].roomId, lectureDto2.roomId)
  }

}