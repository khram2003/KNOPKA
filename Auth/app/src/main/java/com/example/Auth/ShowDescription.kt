package com.example.Auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class ShowDescription(val dialog: Dialog, val item: Knopka, val context: AppCompatActivity) {
    fun showDescription() {
        dialog.setContentView(R.layout.activity_pop_up_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val name = dialog.findViewById<TextView>(R.id.KnopkaName)
        val userProfileInfo = Requests.GetUserProfileInfo(
            context,
            "http://10.0.2.2:8080/api/v1/profile",
            1,
            "111",
            1
        ) //TODO NOT 1
        val mapData: User = jsonFormat.decodeFromString(userProfileInfo.toString())
        name.setText(item.name)

        val author = dialog.findViewById<TextView>(R.id.AuthorName)
        author.setText(mapData.nickname)

        author.setOnClickListener {
            val authorIntent = Intent(context, FriendBioActivity::class.java)
            val authId = item.authorId
            Log.d("AUTHORID", authId.toString())
            authorIntent.putExtra("id", authId.toString())
            startActivity(context, authorIntent, null)
        }
        val knopkaDescr = Requests.GetKnopksaDescr(
            context,
            "http://10.0.2.2:8080/api/v1",
            "111",
            item.id,
            1,
            "knopkaUserId"
        )

        val descriptionText = jsonFormat.decodeFromString<Description>(knopkaDescr)
        Log.d("RES", knopkaDescr)
        Log.d("ALL)", descriptionText.toString())
        val discriptionText = dialog.findViewById<TextView>(R.id.KnopkaDescriptionText)
        discriptionText.setText(descriptionText.text)
        dialog.show()
    }
}