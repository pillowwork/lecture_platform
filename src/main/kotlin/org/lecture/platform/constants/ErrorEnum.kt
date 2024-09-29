package org.lecture.platform.constants

enum class ErrorEnum(val code: Int, val message: String) {

  LECTURE_NO_INFO(1001, "강연(LECTURE) 정보 없음."),

  ROOM_NO_INFO(2001, "강연장(ROOM) 정보 없음."),

}