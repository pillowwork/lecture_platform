package org.lecture.platform.domain.apply.repository

import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplyRepository: JpaRepository<ApplyEntity, Long> {
}