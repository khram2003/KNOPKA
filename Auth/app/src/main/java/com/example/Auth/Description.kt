package com.example.Auth

import kotlinx.serialization.Serializable

@Serializable
data class Description(val text: String = "", val image: Array<Byte>? = null,
                       val tags: List<String>? = null)
