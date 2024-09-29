package org.lecture.platform.back.service

import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BackOfficeService(
  private val lectureRepository: LectureRepository,
  private val roomRepository: RoomRepository,
) {


  fun registerLecture(request: LectureRegisterDto): LectureDto {
    val room = roomRepository.findByIdOrNull(request.roomId)
      ?: throw Exception("강연장(ROOM) 정보 없음.")

    return lectureRepository.save( LectureEntity.makeEntity(request, room) )
      .toDto()
  }

}