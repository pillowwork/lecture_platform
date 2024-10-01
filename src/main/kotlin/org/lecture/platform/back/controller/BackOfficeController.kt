package org.lecture.platform.back.controller

import org.lecture.platform.back.service.BackOfficeService
import org.lecture.platform.constants.PageableConstants
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.lecture.platform.response.ApplicationResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/back")
class BackOfficeController(
  private val backOfficeService: BackOfficeService,
) {

  /** 1. 강연 목록 (전체 강연 목록) */
  @GetMapping("/list")
  fun listLecture(
    @PageableDefault(
      size = PageableConstants.DEFAULT_PAGE_SIZE,
      sort = [PageableConstants.DEFAULT_SORT],
      direction = Sort.Direction.DESC
    ) pageable: Pageable
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      backOfficeService.listLecture(pageable),
      HttpStatus.OK)
  }

  /** 2. 강연 등록 (강연자, 강연장, 신청 인원, 강연 시간, 강연 내용 입력) */
  @PostMapping("/register")
  fun registerLecture(
    @RequestBody request: LectureRegisterDto
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      backOfficeService.registerLecture(request),
      HttpStatus.OK)
  }

  /** 3. 강연 신청자 목록 (강연 별 신청한 사번 목록) */
  @GetMapping("/{lectureId}/members")
  fun memberListLecture(
    @PathVariable lectureId: Long,
    @PageableDefault(
      size = PageableConstants.DEFAULT_PAGE_SIZE,
      sort = [PageableConstants.DEFAULT_SORT],
      direction = Sort.Direction.DESC
    ) pageable: Pageable
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      backOfficeService.memberListLecture(lectureId, pageable),
      HttpStatus.OK)
  }

}