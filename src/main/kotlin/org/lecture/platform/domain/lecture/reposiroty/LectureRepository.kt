package org.lecture.platform.domain.lecture.reposiroty

import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface LectureRepository : JpaRepository<LectureEntity, Long> {

  @Query(
    value = """
      SELECT id, speaker, title, description, start_time, end_time, create_time, update_time, room_id
      FROM lecture
      WHERE start_time <= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 7 DAY)
      AND start_time >= DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 DAY)
    """, nativeQuery = true
  )
  fun findApplicableLectureList(pageable: Pageable): Page<LectureEntity>

  @Query(
    value = """
      SELECT id, speaker, title, description, start_time, end_time, create_time, update_time, room_id
      FROM lecture
      WHERE room_id = :roomId
        AND
        (
          (start_time <= :startTime AND :startTime < end_time)
            OR
          (start_time < :endTime AND :endTime <= end_time)
        )
    """, nativeQuery = true
  )
  fun findLectureByRoomIdAndTimeRange(
    @Param("roomId") roomId: Long ,
    @Param("startTime") startTime: LocalDateTime,
    @Param("endTime") endTime: LocalDateTime
  ): List<LectureEntity>

}