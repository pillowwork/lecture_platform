package org.lecture.platform.domain.lecture.dto

import java.time.LocalDateTime

class LectureDto(
  var id: Long,
  var speaker: String,
  var roomName: String,
  var capacity: Int,
  var title: String,
  var description: String,
  var startTime: LocalDateTime,
  var endTime: LocalDateTime,
  var createTime: LocalDateTime,
  var updateTime: LocalDateTime?,
) {
}
