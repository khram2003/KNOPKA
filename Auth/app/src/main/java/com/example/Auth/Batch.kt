package com.example.Auth

import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
data class Batch(
    var clicks: Long = 0,
    var id : Long = 0,
    var startTime: String = LocalDateTimeEx.getNow(0).toString()
)
