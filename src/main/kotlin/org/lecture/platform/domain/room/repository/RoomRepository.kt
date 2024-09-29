package org.lecture.platform.domain.room.repository

import org.lecture.platform.domain.room.entity.RoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository: JpaRepository<RoomEntity, Long> {
}