package org.lecture.platform.response

import org.lecture.platform.constants.ErrorEnum

class ApplicationErrorResponse(
  val code: Int,
  val message: String
) {

  companion object {

   fun fromEnum(enum: ErrorEnum): ApplicationErrorResponse {
     return ApplicationErrorResponse(
       code = enum.code,
       message = enum.message
     )
   }

    fun noneError() = null

  }
}