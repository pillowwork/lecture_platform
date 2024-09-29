package org.lecture.platform.domain.apply.dto

import org.lecture.platform.domain.lecture.dto.LectureDto
import java.time.LocalDateTime

class ApplyDto (
  var id: Long,
  var employeeId: String,
  var lecture: LectureDto,
  var createTime: LocalDateTime,
) {
}