package dev.triumphteam.kipp.func

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGER: Logger = LoggerFactory.getLogger("dev.triumphteam.kipp")

fun log(message: () -> String) {
    return LOGGER.info(message())
}