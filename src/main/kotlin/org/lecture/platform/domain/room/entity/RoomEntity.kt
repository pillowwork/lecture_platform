package org.lecture.platform.domain.room.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Comment("강연장 정보")
@Table(name = "room")
class RoomEntity (

  // DB sequential id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0L,

  @Comment("강연장 이름")
  @Column(nullable = false)
  var name: String,

  @Comment("입장 가능 최대 인원 수")
  @Column(nullable = false)
  var capacity: Int,

  @CreatedDate
  @Comment("생성 시간")
  @Column(name = "create_time", nullable = false)
  var createTime: LocalDateTime = LocalDateTime.now(),

  @LastModifiedDate
  @Comment("변경 시간")
  @Column(name = "update_time")
  var updateTime: LocalDateTime? = null,

) {
}