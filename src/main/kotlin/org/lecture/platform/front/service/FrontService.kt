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
    return lectureRepository.findByIdOrNull(request.lectureId)?.let {
      // 강연 신청 시, 강연에 이미 신청한 사업이 있는지 확인
      val applyList = applyRepository.findByLectureIdAndEmployeeId(request.lectureId, request.employeeId)
      if(applyList.isEmpty()){
        applyRepository.save(ApplyEntity.makeEntity(request, it))
          .toDto()
      } else {
        throw Exception(ErrorEnum.APPLY_ALREADY_APPLIED.name)
      }
    } ?: throw Exception(ErrorEnum.LECTURE_NO_INFO.name)
    
    // TODO 강의장 별 capacity 적용
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

    return
  }

  fun popularLecture(pageable: Pageable) {

  }

}