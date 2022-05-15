package com.example.Auth
import kotlinx.serialization.*
import android.graphics.Bitmap
@Serializable
data class User(val nickname : String = "", val bio : String = "", val photo : String = ""/*Bitmap*/)