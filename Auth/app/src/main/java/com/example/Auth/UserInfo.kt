package com.example.Auth

data class UserInfo(
    val id: Long = 1,
    val token: String = "111",
    var location: String = "world"
)

object ThisUser {
    lateinit var userInfo: UserInfo
}


data class DbInfo(
    val url: String = "http://10.0.2.2:8080/",
)

object ThisDb {
    lateinit var dbInfo: DbInfo
}