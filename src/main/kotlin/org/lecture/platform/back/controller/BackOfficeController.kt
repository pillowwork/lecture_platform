package org.lecture.platform.back.controller

import org.lecture.platform.back.service.BackOfficeService
import org.lecture.platform.domain.lecture.dto.LectureDto
import org.lecture.platform.domain.lecture.dto.LectureRegisterDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/back")
class BackOfficeController(
  private val backOfficeService: BackOfficeService,
) {

  /** 1. 강연 목록 (전체 강연 목록) */
  fun listLecture() {
  }

  /** 2. 강연 등록 (강연자, 강연장, 신청 인원, 강연 시간, 강연 내용 입력) */
  @PostMapping("/register")
  fun registerLecture(@RequestBody request: LectureRegisterDto): ResponseEntity<LectureDto> {
    return ResponseEntity(backOfficeService.registerLecture(request), HttpStatus.OK)
  }

  /** 3. 강연 신청자 목록 (강연 별 신청한 사번 목록) */
  fun memberListLecture() {
  }

}