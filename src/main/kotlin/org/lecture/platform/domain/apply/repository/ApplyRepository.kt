package org.lecture.platform.domain.apply.repository

import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplyRepository: JpaRepository<ApplyEntity, Long> {

  fun findByLectureId(lectureId: Long, pageable: Pageable): Page<ApplyEntity>

  fun findByEmployeeId(employeeId: String, pageable: Pageable): Page<ApplyEntity>

}