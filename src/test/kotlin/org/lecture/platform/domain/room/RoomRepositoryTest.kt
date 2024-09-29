package org.lecture.platform.domain.room

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.lecture.platform.domain.room.entity.RoomEntity
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class RoomRepositoryTest {

  private lateinit var roomRepository: RoomRepository

  @BeforeEach
  fun setUp() {
    roomRepository = mockk<RoomRepository>()
  }

  @Test
  fun registerTest() {
    // set
    val entity = RoomEntity(
      id = 1,
      name = "강연장 1",
      capacity = 5,
    )
    every { roomRepository.save(entity) } returns entity

    // when
    val savedEntity = roomRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.name ,"강연장 1")
    assertEquals(savedEntity.capacity ,5)
  }

  @Test
  fun readTest() {
    // set
    val entity = RoomEntity(
      id = 1,
      name = "강연장 1",
      capacity = 5,
    )
    every { roomRepository.findByIdOrNull(entity.id) } returns entity

    // when
    val savedEntity = roomRepository.findByIdOrNull(1)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertEquals(savedEntity.name ,"강연장 1")
    assertEquals(savedEntity.capacity ,5)
  }

  @Test
  fun modifyTest() {
    // set
    val entity = RoomEntity(
      id = 1,
      name = "강연장 1",
      capacity = 5,
    )
    every { roomRepository.save(entity) } returns entity

    // when
    entity.name = "강연장 A"
    entity.capacity = 50
    val savedEntity = roomRepository.save(entity)

    // then
    assertNotNull(savedEntity)
    assertEquals(savedEntity.id ,1)
    assertNotEquals(savedEntity.name ,"강연장 1")
    assertNotEquals(savedEntity.capacity ,5)
    assertEquals(savedEntity.name ,"강연장 A")
    assertEquals(savedEntity.capacity ,50)
  }

  @Test
  fun removeTest() {
    // set
    val entity = RoomEntity(
      id = 1,
      name = "강연장 1",
      capacity = 5,
    )
    every { roomRepository.delete(entity) } just runs

    // when
    roomRepository.delete(entity)

    // then
    verify { roomRepository.delete(entity) }
  }

  @Test
  fun listTest() {
    // set
    val entity1 = RoomEntity(
      id = 1,
      name = "강연장 1",
      capacity = 5,
    )
    val entity2 = RoomEntity(
      id = 2,
      name = "강연장 2",
      capacity = 10,
    )
    val entity3 = RoomEntity(
      id = 3,
      name = "강연장 3",
      capacity = 15,
    )
    val list = arrayListOf(entity1, entity2, entity3)
    every { roomRepository.findAll() } returns list

    // when
    val savedList = roomRepository.findAll()

    // then
    assertNotNull(savedList)
    assertTrue(savedList.isNotEmpty())
    assertEquals(savedList.size ,3)
    assertTrue(savedList.contains(entity1))
    assertTrue(savedList.contains(entity2))
    assertTrue(savedList.contains(entity3))
  }
}