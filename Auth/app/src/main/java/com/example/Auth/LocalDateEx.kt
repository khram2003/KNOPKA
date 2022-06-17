package com.example.Auth

import org.threeten.bp.LocalDate
import java.util.*

object LocalDateEx {
    /**an alternative of LocalDate.now(), as it requires initialization using AndroidThreeTen.init(context), which takes a bit time (loads a file)*/
    @JvmStatic
    fun getNow(): LocalDate = Calendar.getInstance().toLocalDate()
}

fun Calendar.toLocalDate(): LocalDate = LocalDate.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))