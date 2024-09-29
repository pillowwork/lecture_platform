package org.lecture.platform.front.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/front")
class FrontController {

  /** 1. 강연 목록 (신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출) */
  fun listLecture() {
  }

  /** 2. 강연 신청 (사업 입력, 같은 강연 중복 신청 제한) */
  fun applyLecture() {
  }

  /** 3. 신청 내역 조회 (사업 입력) */
  fun applyListLecture() {
  }

  /** 4. 신청한 강연 취소 */
  fun applyCancelLecture() {
  }

  /** 5. 실시간 인기 강연 */
  fun popularLecture() {
  }

}