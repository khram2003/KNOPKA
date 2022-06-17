package com.example.Auth

data class UserInfo(
    val id: Long = 1,
    val token: String = "111",
    var location: String = "world"
)

object ThisUser {
    lateinit var userInfo : UserInfo
}
