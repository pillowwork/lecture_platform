package org.lecture.platform.domain.lecture.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.room.entity.RoomEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Comment("강연 정보")
@Table(name = "lecture")
class LectureEntity (

  // DB sequential id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0L,

  @Comment("강연자")
  @Column(nullable = false)
  var speaker: String,

  @Comment("강연 제목")
  @Column(nullable = false)
  var title: String,

  @Comment("강연 내용")
  @Column(nullable = false)
  var description: String,

  @Comment("강연 시작 시간")
  @Column(name="start_time", nullable = false)
  var startTime: LocalDateTime,

  @Comment("강연 종료 시간")
  @Column(name="end_time", nullable = false)
  var endTime: LocalDateTime,

  @CreatedDate
  @Comment("생성 시간")
  @Column(name = "create_time", nullable = false)
  var createTime: LocalDateTime = LocalDateTime.now(),

  @LastModifiedDate
  @Comment("변경 시간")
  @Column(name = "update_time")
  var updateTime: LocalDateTime? = null,

  @Comment("강연장 정보")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  var room: RoomEntity,

  ) {

  companion object {

    fun makeEntity(request: LectureRegisterDto, room: RoomEntity): LectureEntity {
      return LectureEntity(
        speaker = request.speaker,
        title = request.title,
        description = request.description,
        startTime = request.startTime,
        endTime = request.endTime,
        room = room,
      )

    }

  }

  fun toDto(): LectureDto {
    return LectureDto(
      id = this.id,
      speaker = this.speaker,
      title = this.title,
      description = this.description,
      startTime = this.startTime,
      endTime = this.endTime,
      createTime = this.createTime,
      updateTime = this.updateTime,
      roomId = this.room.id,
    )
  }

}
