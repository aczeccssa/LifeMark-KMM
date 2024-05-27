package data.units

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Central Standard Time -> CST, conforms to ISO 8601:2004, same as UTC+8
 * LifeMark develop on China, using `Beijing` time zone as same as `Asia/Shanghai`.
 * Is `Asia/Shanghai`
 * @see <a href="https://www.timeanddate.com/time/zones/cst">CST</a>
 */
val TimeZone.Companion.CST: TimeZone
    get() = of("Asia/Shanghai")

/**
 * Get current current time in the type of `LocalDateTime`
 * @parma timeZone [TimeZone] Set the time zone, default is `Asia/Shanghai`.
 */
fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.CST): LocalDateTime {
    val currentMoment = Clock.System.now()
    return currentMoment.toLocalDateTime(timeZone)
}

/**
 * Get current current time in the type of `LocalDate`
 * @parma timeZone [TimeZone] Set the time zone, default is `Asia/Shanghai`.
 */
fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.CST): LocalDate {
    return LocalDateTime.now(timeZone).date
}

/**
 * Get current current time in the type of `LocalTime`
 * @parma timeZone [TimeZone] Set the time zone, default is `Asia/Shanghai`.
 */
fun LocalTime.Companion.now(timeZone: TimeZone = TimeZone.CST): LocalTime {
    return LocalDateTime.now(timeZone).time
}

//
val LocalDateTime.Companion.ISO_ZERO: LocalDateTime
    get() = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.parse("1970-01-01T00:00:00Z").toLocalDateTime()

val LocalDate.Companion.ISO_ZERO: LocalDate
    get() = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.parse("1970-01-01T00:00:00Z").toLocalDate()

val Clock.Companion.ISO_ZERO: Instant
    get() = LocalDateTime.ISO_ZERO.toInstant(TimeZone.CST)