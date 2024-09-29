package org.lecture.platform.domain.lecture

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.entity.RoomEntity
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class LectureRepositoryTest {

  private lateinit var lectureRepository: LectureRepository
  private lateinit var room: RoomEntity
  private lateinit var now: LocalDateTime

  @BeforeEach
  fun setUp() {
    lectureRepository = mockk<LectureRepository>()
    room = RoomEntity(
      id = 1,
      name = "강의실 A",
      capacity = 200
    )
    now = LocalDateTime.now()
  }

  @Test
  fun registerTest() {
    // set
    val entity = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    every { lectureRepository.save(entity) } returns entity

    // when
    val savedEntity = lectureRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.speaker ,"김창옥")
    assertEquals(savedEntity.title ,"포프리쇼")
    assertEquals(savedEntity.description ,"행복한 순간을 기억해주세요")
  }

  @Test
  fun readTest() {
    // set
    val entity = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    every { lectureRepository.findByIdOrNull(entity.id) } returns entity

    // when
    val savedEntity = lectureRepository.findByIdOrNull(1)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.speaker ,"김창옥")
    assertEquals(savedEntity.title ,"포프리쇼")
    assertEquals(savedEntity.description ,"행복한 순간을 기억해주세요")
  }

  @Test
  fun modifyTest() {
    // set
    val entity = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    every { lectureRepository.save(entity) } returns entity

    // when
    entity.speaker = "김미경"
    val savedEntity = lectureRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertNotEquals(savedEntity.speaker ,"김창옥")
    assertEquals(savedEntity.speaker ,"김미경")
  }

  @Test
  fun removeTest() {
    // set
    val entity = LectureEntity(
      id = 1,
      speaker = "김창옥",
      title = "포프리쇼",
      description = "행복한 순간을 기억해주세요",
      startTime = now,
      endTime = now.plusHours(2),
      room = room
    )
    every { lectureRepository.delete(entity) } just runs

    // when
    lectureRepository.delete(entity)

    // then
    verify { lectureRepository.delete(entity) }
  }

  @Test
  fun listTest() {
    // set
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
      title = "인생면언",
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
    val list = arrayListOf(entity1, entity2, entity3)
    every { lectureRepository.findAll() } returns list

    // when
    val savedList = lectureRepository.findAll()

    // then
    assertNotNull(savedList)
    assertTrue(savedList.isNotEmpty())
    assertEquals(savedList.size ,3)
    assertTrue(savedList.contains(entity1))
    assertTrue(savedList.contains(entity2))
    assertTrue(savedList.contains(entity3))
  }
}