package org.lecture.platform.exception

import org.lecture.platform.constants.ErrorEnum
import org.lecture.platform.response.ApplicationErrorResponse
import org.lecture.platform.response.ApplicationResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  companion object {
    val logger = LoggerFactory.getLogger(this::class.java)
  }

  @ExceptionHandler(Exception::class)
  fun handleException(ex: Exception): ResponseEntity<ApplicationResponse> {
    logger.error(ex.message)
    return ex.message?.let {
      ResponseEntity(
        ApplicationResponse.makeResponse(
          ApplicationResponse.noneData(),
          ApplicationErrorResponse.fromEnum( ErrorEnum.valueOf(it) )
        ),
        HttpStatus.BAD_REQUEST)
    } ?: ResponseEntity(HttpStatus.BAD_REQUEST)
  }
}