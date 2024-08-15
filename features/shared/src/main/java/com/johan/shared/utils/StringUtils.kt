package com.johan.shared.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun String.toFullPosterURL() = "${URL.POSTER_URL}$this"

object URL {
    const val POSTER_URL = "https://image.tmdb.org/t/p/w500/"
}

fun String.formatDateToMonthAndYear(): String {
    return LocalDate.parse(this).format(LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    })
}