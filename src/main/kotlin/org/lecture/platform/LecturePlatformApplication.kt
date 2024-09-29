package org.lecture.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LecturePlatformApplication

fun main(args: Array<String>) {
  runApplication<LecturePlatformApplication>(*args)
}