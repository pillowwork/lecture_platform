package org.lecture.platform.constants

enum class ErrorEnum(val code: Int, val message: String) {

  LECTURE_NO_INFO(1001, "강연(LECTURE) 정보 없음."),
  LECTURE_TIME_OVERLAP(1002, "입력 시간대에 강연장(ROOM)에 강연(LECTURE)이 있음."),

  ROOM_NO_INFO(2001, "강연장(ROOM) 정보 없음."),

}