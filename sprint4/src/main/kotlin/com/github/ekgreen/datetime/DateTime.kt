package com.github.ekgreen.datetime

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


// 1. Получить сет часовых поясов, которые используют смещение от UTC не в полных часах
fun getZonesWithNonDivisibleByHourOffset(): Set<String> {
    val now: Instant = Instant.now(Clock.systemUTC())
    return ZoneId.getAvailableZoneIds()
        .map { ZoneId.of(it) }
        .filter { it.rules.getStandardOffset(now).totalSeconds % 3600 != 0 }
        .map { it.id }
        .toSet()
}

fun main(){
    val now: Instant = Instant.now(Clock.systemUTC())

    println(ZoneId.of("Australia/LHI").rules.getStandardOffset(now))
}

// 2. Для заданного года вывести список, каким днем недели был последний день в месяце.
fun getLastInMonthDayWeekList(year: Int): List<String> {
    return Month.values().map { LocalDate.of(year, it, 1).with(TemporalAdjusters.lastDayOfMonth()).dayOfWeek.name }
}

// 3. Для заданного года вывести количество дней, выпадающих на пятницу 13-ое
fun getNumberOfFridayThirteensInYear(year: Int): Int {
    return Month.values()
        .map { LocalDate.of(year, it, 13).dayOfWeek }
        .count { it == DayOfWeek.FRIDAY }
}

// 4. Вывести заданную дату в формате "01 Aug 2021, 23:39", в котором дата локализована для вывода в США (US)
fun getFormattedDateTime(dateTime: LocalDateTime): String {
    return dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.US))
}



