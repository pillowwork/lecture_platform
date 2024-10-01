package org.lecture.platform.front.controller

import org.lecture.platform.constants.PageableConstants
import org.lecture.platform.domain.apply.dto.ApplyDto
import org.lecture.platform.domain.apply.dto.ApplyRequestDto
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.front.service.FrontService
import org.lecture.platform.response.ApplicationErrorResponse
import org.lecture.platform.response.ApplicationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/front")
class FrontController(
  private val frontService: FrontService,
) {

  /** 1. 강연 목록 (신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출) */
  @GetMapping("/list")
  fun listLecture(
    @PageableDefault(
      size = PageableConstants.DEFAULT_PAGE_SIZE,
      sort = [PageableConstants.DEFAULT_SORT],
      direction = Sort.Direction.DESC
    ) pageable: Pageable
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      frontService.listLecture(pageable),
      HttpStatus.OK)
  }

  /** 2. 강연 신청 (사업 입력, 같은 강연 중복 신청 제한) */
  @PostMapping("/apply")
  fun applyLecture(
    @RequestBody request: ApplyRequestDto
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      frontService.applyLecture(request),
      HttpStatus.OK)
  }

  /** 3. 신청 내역 조회 (사번 입력) */
  @GetMapping("/list/{employeeId}")
  fun applyListLecture(
    @PathVariable employeeId: String,
    @PageableDefault(
      size = PageableConstants.DEFAULT_PAGE_SIZE,
      sort = [PageableConstants.DEFAULT_SORT],
      direction = Sort.Direction.DESC
    ) pageable: Pageable
  ): ResponseEntity<ApplicationResponse> {
    return ApplicationResponse.makeResponseEntity(
      frontService.applyListLecture(employeeId, pageable),
      HttpStatus.OK)
  }

  /** 4. 신청한 강연 취소 */
  @DeleteMapping("/apply/{applyId}/cancel")
  fun applyCancelLecture(
    @PathVariable applyId: Long,
  ): ResponseEntity<Any> {
    frontService.applyCancelLecture(applyId)
    return ResponseEntity(HttpStatus.OK)
  }

  /** 5. 실시간 인기 강연 */
  @GetMapping("/popular")
  fun popularLecture(
    @PageableDefault(
      size = PageableConstants.DEFAULT_PAGE_SIZE,
      sort = [PageableConstants.DEFAULT_SORT],
      direction = Sort.Direction.DESC
    ) pageable: Pageable
  ) {
    frontService.popularLecture(pageable)
  }

}