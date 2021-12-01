package dev.triumphteam.kipp.scheduler

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.EnumSet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

val EVERYDAY = EnumSet.allOf(DayOfWeek::class.java)

val MINUTES_TILL_MIDNIGHT: Duration
    get() = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES).minutes
