package dev.triumphteam.kipp.scheduler

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


val MINUTES_TILL_MIDNIGHT: Duration
    get() = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES).minutes
