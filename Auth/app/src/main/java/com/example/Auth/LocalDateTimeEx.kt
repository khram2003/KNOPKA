package com.example.Auth

import org.threeten.bp.LocalDateTime
import java.util.*

object LocalDateTimeEx {
    /**an alternative of LocalDateTime.now(), as it requires initialization using AndroidThreeTen.init(context), which takes a bit time (loads a file)*/
    @JvmStatic
    fun getNow(x : Int): LocalDateTime = Calendar.getInstance().toLocalDateTime(x)
}

private fun Calendar.toLocalDateTime(x : Int): LocalDateTime = LocalDateTime.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH), get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.SECOND),
    get(Calendar.MILLISECOND) * 1000000 + x * 1000000)