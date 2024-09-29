package org.lecture.platform.domain.apply.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Comment("강연 신청 내역")
@Table(name = "apply")
class ApplyEntity (

  // DB sequential id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0L,

  @Comment("사번")
  @Column(name="employee_id",nullable = false)
  var employeeId: String,

  @Comment("강연 ID")
  @Column(name = "lecture_id", nullable = false)
  var lectureId: Long,

  @CreatedDate
  @Column(name = "create_time", nullable = false)
  var createTime: LocalDateTime = LocalDateTime.now(),

) {
}