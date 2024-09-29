package org.lecture.platform.back.service

import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BackOfficeService(
  private val applyRepository: ApplyRepository,
  private val lectureRepository: LectureRepository,
  private val roomRepository: RoomRepository,
) {

  fun listLecture(pageable: Pageable): Page<LectureDto> {
    val entityPage = lectureRepository.findAll(pageable)
    val dtoList = entityPage.map {
      it.toDto()
    }
    return PageImpl(dtoList.content, pageable, entityPage.totalElements)
  }

  fun registerLecture(request: LectureRegisterDto): LectureDto {
    val room = roomRepository.findByIdOrNull(request.roomId)
      ?: throw Exception(ErrorEnum.ROOM_NO_INFO.message)

    return lectureRepository.save( LectureEntity.makeEntity(request, room) )
      .toDto()
  }

  fun memberListLecture(lectureId: Long, pageable: Pageable): Page<ApplyDto> {
    val entityPage = applyRepository.findByLectureId(lectureId, pageable)
    val dtoList = entityPage.map {
      it.toDto()
    }
    return PageImpl(dtoList.content, pageable, entityPage.totalElements)
  }


}