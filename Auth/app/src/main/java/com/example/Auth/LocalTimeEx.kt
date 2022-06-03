package com.example.Auth

import org.threeten.bp.LocalTime
import java.util.*

object LocalTimeEx {
    /**an alternative of LocalDateTime.now(), as it requires initialization using AndroidThreeTen.init(context), which takes a bit time (loads a file)*/
    @JvmStatic
    fun getNow(): LocalTime = Calendar.getInstance().toLocalTime()
}

fun Calendar.toLocalTime(): LocalTime = LocalTime.of(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.SECOND), get(Calendar.MILLISECOND) * 1000000)