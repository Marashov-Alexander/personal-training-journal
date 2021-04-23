package ru.ok.technopolis.training.personal.utils

enum class Weekday(val value: Int) {
    MONDAY(1 shl 0),
    TUESDAY(1 shl 1),
    WEDNESDAY(1 shl 2),
    THURSDAY(1 shl 3),
    FRIDAY(1 shl 4),
    SATURDAY(1 shl 5),
    SUNDAY(1 shl 6),
}

fun getWeek(): List<Pair<Triple<Int, Int, Int>, Weekday>> {
    return listOf(
            Triple(2020, 12, 21) to Weekday.MONDAY,
            Triple(2020, 12, 22) to Weekday.TUESDAY,
            Triple(2020, 12, 23) to Weekday.WEDNESDAY,
            Triple(2020, 12, 24) to Weekday.THURSDAY,
            Triple(2020, 12, 25) to Weekday.FRIDAY,
            Triple(2020, 12, 26) to Weekday.SATURDAY,
            Triple(2020, 12, 27) to Weekday.SUNDAY
    )
}