package org.lecture.platform.exception

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
  fun handleException(ex: Exception): ResponseEntity<Any> {
    logger.error(ex.message)
    return ResponseEntity(
      mapOf(
        "status" to HttpStatus.BAD_REQUEST,
        "message" to ex.message,
      ),
      HttpStatus.BAD_REQUEST)
  }
}