package org.lecture.platform.domain.lecture.reposiroty

import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureRepository: JpaRepository<LectureEntity, Long> {
}