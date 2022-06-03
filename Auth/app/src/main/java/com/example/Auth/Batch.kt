package com.example.Auth

import org.threeten.bp.LocalDateTime

data class Batch(
    var time: String = "",
    var pushes: Long = 0,
    var region: String = "",
    var clickedKnopkaId: Long = 0

)
