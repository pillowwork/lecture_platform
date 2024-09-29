package org.lecture.platform.domain.apply.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
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

  @CreatedDate
  @Column(name = "create_time", nullable = false)
  var createTime: LocalDateTime = LocalDateTime.now(),

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  var lecture: LectureEntity,

) {

  fun toDto(): ApplyDto {
    return ApplyDto(
      id = this.id,
      employeeId = this.employeeId,
      createTime = this.createTime,
      lectureId = this.lecture.id,
    )
  }

}