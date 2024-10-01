package org.lecture.platform.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    fun makeResponseEntity(data: Any?, httpStatus: HttpStatus): ResponseEntity<ApplicationResponse> {
      return ResponseEntity(
        this.makeResponse(
          data,
          ApplicationErrorResponse.noneError()
        ),
        httpStatus)
    }

    fun makeResponseEntity( httpStatus: HttpStatus): ResponseEntity<ApplicationResponse> {
      return ResponseEntity(
        this.makeResponse(
          noneData(),
          ApplicationErrorResponse.noneError()
        ),
        httpStatus)
    }

    fun noneData() = null

  }

}