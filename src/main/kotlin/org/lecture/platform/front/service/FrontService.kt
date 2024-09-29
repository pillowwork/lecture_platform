package org.lecture.platform.front.service

import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.apply.dto.ApplyRequestDto
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.lecture.platform.domain.room.repository.RoomRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FrontService(
  private val applyRepository: ApplyRepository,
  private val lectureRepository: LectureRepository,
) {

  fun listLecture(pageable: Pageable): Page<LectureDto> {
    val entityPage = lectureRepository.findApplicableLectureList(pageable)
    val dtoList = entityPage.map {
      it.toDto()
    }
    return PageImpl(dtoList.content, pageable, entityPage.totalElements)
  }

  fun applyLecture(request: ApplyRequestDto): ApplyDto {
    return lectureRepository.findByIdOrNull(request.lectureId)?.let {
      applyRepository.save(ApplyEntity.makeEntity(request, it))
        .toDto()
    } ?: throw Exception(ErrorEnum.LECTURE_NO_INFO.message)

    // TODO 이미 신청 체크 (중복 체크)
  }




  fun applyListLecture(employeeId: String, pageable: Pageable): Page<ApplyDto> {
    return applyRepository.findByEmployeeId(employeeId, pageable)
      .map {
        it.toDto()
      }
  }

  fun applyCancelLecture(applyId: Long) {
    applyRepository.findByIdOrNull(applyId)?.let {
      applyRepository.delete(it)
    }
  }

  fun popularLecture(pageable: Pageable) {

  }

}