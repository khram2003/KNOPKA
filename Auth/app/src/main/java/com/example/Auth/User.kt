package com.example.Auth
import kotlinx.serialization.*

@Serializable
data class User(val nickname : String = "", val bio : String = "", val photo : String = "", val id : Int = 1)