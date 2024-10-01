package org.lecture.platform.front.service

import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.apply.dto.ApplyRequestDto
import org.lecture.platform.domain.apply.entity.ApplyEntity
import org.lecture.platform.domain.apply.repository.ApplyRepository
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.reposiroty.LectureRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

  @Transactional
  fun applyLecture(request: ApplyRequestDto): ApplyDto {
    val lecture = lectureRepository.findByIdOrNull(request.lectureId)
      ?: throw Exception(ErrorEnum.LECTURE_NO_INFO.name)

    if (applyRepository.countByLectureId(lecture.id) >=  lecture.capacity) {
      throw Exception(ErrorEnum.LECTURE_CAPACITY_FULL.name)
    }

    val applyList = applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId)
    if(applyList.isNotEmpty()){
      throw Exception(ErrorEnum.APPLY_ALREADY_APPLIED.name)
    }

    return applyRepository.save(ApplyEntity.makeEntity(request, lecture))
      .toDto()
  }

  fun applyListLecture(employeeId: String, pageable: Pageable): Page<ApplyDto> {
    return applyRepository.findByEmployeeId(employeeId, pageable)
      .map {
        it.toDto()
      }
  }

  @Transactional
  fun applyCancelLecture(applyId: Long) {
    applyRepository.findByIdOrNull(applyId)?.let {
      applyRepository.delete(it)
    } ?: throw Exception(ErrorEnum.APPLY_NO_INFO.name)
  }

  fun popularLecture(pageable: Pageable): Page<LectureDto> {
    val entityPage = lectureRepository.findApplyByPopular(pageable)
    val dtoList = entityPage.map {
      it.toDto()
    }
    return PageImpl(dtoList.content, pageable, entityPage.totalElements)
  }

}