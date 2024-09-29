package org.lecture.platform.domain.lecture.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class LectureRegisterDto(
  var speaker: String,
  var title: String,
  var description: String,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime: LocalDateTime,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var endTime: LocalDateTime,
  var roomId: Long,
) {
}
