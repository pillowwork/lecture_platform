package org.lecture.platform.domain.lecture.dto

import java.time.LocalDateTime

class LectureRegisterDto(
  var speaker: String,
  var title: String,
  var description: String,
  var startTime: LocalDateTime,
  var endTime: LocalDateTime,
  var roomId: Long,
) {
}
