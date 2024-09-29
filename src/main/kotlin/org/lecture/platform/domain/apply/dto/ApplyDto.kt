package org.lecture.platform.domain.apply.dto

import java.time.LocalDateTime

class ApplyDto (
  var id: Long,
  var employeeId: String,
  var lectureId: Long,
  var createTime: LocalDateTime,
) {
}