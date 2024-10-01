package org.lecture.platform.back.service

import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.domain.lecture.entity.LectureEntity
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BackOfficeService(
  private val applyRepository: ApplyRepository,
  private val lectureRepository: LectureRepository,
) {

  fun listLecture(pageable: Pageable): Page<LectureDto> {
    val entityPage = lectureRepository.findAll(pageable)
    val dtoList = entityPage.map {
      it.toDto()
    }
    return PageImpl(dtoList.content, pageable, entityPage.totalElements)
  }

  @Transactional
  fun registerLecture(request: LectureRegisterDto): LectureDto {
    // 강연 등록 시, 강연장에 동 시간 강연이 있는지 확인
    val lectureList = lectureRepository.findLectureByRoomNameAndTimeRange(request.roomName, request.startTime, request.endTime)
    if(lectureList.isEmpty()) {
      return lectureRepository.save( LectureEntity.makeEntity(request) )
        .toDto()
    } else {
      throw Exception(ErrorEnum.LECTURE_TIME_OVERLAP.name)
    }
  }

  fun memberListLecture(lectureId: Long, pageable: Pageable): Page<ApplyDto> {
    lectureRepository.findByIdOrNull(lectureId)?.let {
      val entityPage = applyRepository.findByLectureId(lectureId, pageable)
      val dtoList = entityPage.map {
        it.toDto()
      }
      return PageImpl(dtoList.content, pageable, entityPage.totalElements)
    } ?: throw Exception(ErrorEnum.LECTURE_NO_INFO.name)
  }

}