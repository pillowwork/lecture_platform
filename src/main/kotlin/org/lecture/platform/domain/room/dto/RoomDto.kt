package org.lecture.platform.domain.room.dto

import java.time.LocalDateTime

class RoomDto (
  var id: Long,
  var name: String,
  var capacity: Int,
  var createTime: LocalDateTime,
  var updateTime: LocalDateTime?,
) {
}