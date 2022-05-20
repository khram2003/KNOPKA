package com.example.Auth

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.*

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

object Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun bitMapToBase64String(bitMap: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitMap?.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        // TODO: what quality to use?
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(imageBytes)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun base64StringToBitMap(str: String?): Bitmap? {
        val imageBytes = Base64.getDecoder().decode(str)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun stringToButtons(knopkasString: String): List<Knopka> =
        jsonFormat.decodeFromString(knopkasString)

    fun stringToUsers(knopkasString: String): List<User> =
        jsonFormat.decodeFromString(knopkasString)
}