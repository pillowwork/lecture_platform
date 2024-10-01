package org.lecture.platform.response

import java.time.LocalDateTime

class ApplicationResponse(
  val timestamp: LocalDateTime = LocalDateTime.now(),
  val data: Any?,
  val error: ApplicationErrorResponse?
) {

  companion object {

    fun makeResponse(data: Any?, error: ApplicationErrorResponse?): ApplicationResponse {
      return ApplicationResponse(
        data = data,
        error = error
      )
    }

    fun noneData() = null

  }

}