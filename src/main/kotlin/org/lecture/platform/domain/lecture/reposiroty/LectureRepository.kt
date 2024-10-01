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
      SELECT id, speaker, room_name, capacity, title, description, start_time, end_time, create_time, update_time
      FROM lecture
      WHERE start_time <= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 7 DAY)
      AND start_time >= DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 DAY)
    """, nativeQuery = true
  )
  fun findApplicableLectureList(pageable: Pageable): Page<LectureEntity>

  @Query(
    value = """
      SELECT id, speaker, room_name, capacity, title, description, start_time, end_time, create_time, update_time
      FROM lecture
      WHERE room_name = :roomName
        AND
        (
          (start_time <= :startTime AND :startTime < end_time)
            OR
          (start_time < :endTime AND :endTime <= end_time)
        )
    """, nativeQuery = true
  )
  fun findLectureByRoomNameAndTimeRange(
    @Param("roomName") roomName: String ,
    @Param("startTime") startTime: LocalDateTime,
    @Param("endTime") endTime: LocalDateTime
  ): List<LectureEntity>

  @Query(
    value = """
      SELECT A.id, A.speaker, A.room_name, A.capacity, A.title, A.description, A.start_time, A.end_time, A.create_time, A.update_time
      FROM lecture A,
        (
          SELECT B.lecture_id, COUNT(B.employee_id) AS apply_cnt
          FROM apply B
          WHERE B.create_time >= DATE_SUB(NOW(), INTERVAL 3 DAY)
          GROUP BY B.lecture_id
        ) Z
      WHERE A.id = Z.lecture_id
      ORDER BY Z.apply_cnt DESC
    """, nativeQuery = true
  )
  fun findApplyByPopular(pageable: Pageable): Page<LectureEntity>

}