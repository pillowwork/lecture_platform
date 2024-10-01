package org.lecture.platform.constants

enum class ErrorEnum(val code: Int, val message: String) {

  LECTURE_NO_INFO(1001, "강연(LECTURE) 정보 없음."),
  LECTURE_TIME_OVERLAP(1002, "입력 시간대에 강연장에 강연(LECTURE)이 있음."),
  LECTURE_CAPACITY_FULL(1003, "강연(LECTURE) 신청 인원 마감."),

  APPLY_ALREADY_APPLIED(2001, "강연(LECTURE)에 이미 신청(APPLY) 정보가 있음."),
  APPLY_NO_INFO(2002, "신청(APPLY) 정보 없음."),

}