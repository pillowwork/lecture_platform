package org.lecture.platform.domain.apply

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.room.entity.RoomEntity
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class ApplyRepositoryTest {

  private lateinit var applyRepository: ApplyRepository
  private lateinit var room: RoomEntity
  private lateinit var now: LocalDateTime
  private lateinit var lecture: LectureEntity

  @BeforeEach
  fun setUp() {
    applyRepository = mockk<ApplyRepository>()
    room = RoomEntity(
      id = 1,
      name = "강의실 A",
      capacity = 200
    )
    now = LocalDateTime.now()
    lecture = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
  }

  @Test
  fun registerTest() {
    // set
    val entity = ApplyEntity(
      id = 1,
      employeeId = "12345",
      lecture = lecture,
    )
    every { applyRepository.save(entity) } returns entity

    // when
    val savedEntity = applyRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.employeeId ,"12345")
    assertEquals(savedEntity.lecture.id ,1)
  }

  @Test
  fun readTest() {
    // set
    val entity = ApplyEntity(
      id = 1,
      employeeId = "12345",
      lecture = lecture,
    )
    every { applyRepository.findByIdOrNull(entity.id) } returns entity

    // when
    val savedEntity = applyRepository.findByIdOrNull(1)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.employeeId ,"12345")
    assertEquals(savedEntity.lecture.id ,1)
  }

  @Test
  fun modifyTest() {
    // set
    val entity = ApplyEntity(
      id = 1,
      employeeId = "12345",
      lecture = lecture,
    )
    every { applyRepository.save(entity) } returns entity

    // when
    entity.employeeId = "56458"
    entity.lecture.id = 2
    val savedEntity = applyRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertNotEquals(savedEntity.employeeId ,"12345")
    assertNotEquals(savedEntity.lecture.id ,1)
    assertEquals(savedEntity.employeeId ,"56458")
    assertEquals(savedEntity.lecture.id ,2)
  }

  @Test
  fun removeTest() {
    // set
    val entity = ApplyEntity(
      id = 1,
      employeeId = "12345",
      lecture = lecture,
    )
    every { applyRepository.delete(entity) } just runs

    // when
    applyRepository.delete(entity)

    // then
    verify { applyRepository.delete(entity) }
  }

  @Test
  fun listTest() {
    // set
    val entity1 = ApplyEntity(
      id = 1,
      employeeId = "12345",
      lecture = lecture,
    )
    val entity2 = ApplyEntity(
      id = 2,
      employeeId = "67890",
      lecture = lecture,
    )
    val entity3= ApplyEntity(
      id = 3,
      employeeId = "95175",
      lecture = lecture,
    )
    val list = arrayListOf(entity1, entity2, entity3)
    every { applyRepository.findAll() } returns list

    // when
    val savedList = applyRepository.findAll()

    // then
    assertNotNull(savedList)
    assertTrue(savedList.isNotEmpty())
    assertEquals(savedList.size ,3)
    assertTrue(savedList.contains(entity1))
    assertTrue(savedList.contains(entity2))
    assertTrue(savedList.contains(entity3))
  }
}