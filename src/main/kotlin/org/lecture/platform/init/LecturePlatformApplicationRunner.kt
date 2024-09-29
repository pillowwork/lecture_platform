package org.lecture.platform.init

import org.lecture.platform.domain.room.entity.RoomEntity
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LecturePlatformApplicationRunner(
  private val roomRepository: RoomRepository,
  ): ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    /** 강연장 정보 기본 설정 */
    roomRepository.saveAllAndFlush(
      arrayListOf(
        RoomEntity(name = "강연장 1", capacity = 5),
        RoomEntity(name = "강연장 2", capacity = 10),
        RoomEntity(name = "강연장 3", capacity = 15),
        RoomEntity(name = "강연장 4", capacity = 20),
        RoomEntity(name = "강연장 5", capacity = 25)
      )
    )
  }
  
}