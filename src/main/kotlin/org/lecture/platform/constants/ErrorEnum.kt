package org.lecture.platform.constants

enum class ErrorEnum(val code: Int, val message: String) {

  LECTURE_NO_INFO(1001, "강연(LECTURE) 정보 없음."),
  LECTURE_TIME_OVERLAP(1002, "입력 시간대에 강연장에 강연(LECTURE)이 있음."),

  ROOM_NO_INFO(2001, "강연장(ROOM) 정보 없음."),

  APPLY_ALREADY_APPLIED(3001, "강연(LECTURE)에 이미 신청(APPLY) 정보가 있음."),
  APPLY_NO_INFO(3002, "신청(APPLY) 정보 없음."),

}