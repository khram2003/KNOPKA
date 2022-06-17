package com.example.Auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class ShowDescription : AppCompatActivity(), OnTagClickListener {
    //    lateinit private var binding: ActivityPopUpInfoBinding
    private val adapter = TagAdapter(this)
    lateinit var context: AppCompatActivity

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        Log.d("layoutinfl", inflater.toString())
//        binding = ActivityPopUpInfoBinding.inflate(inflater)
    }

    fun showDescription(dialog: Dialog, item: Knopka, cnxt: AppCompatActivity) {
        context = cnxt

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
        name.text = item.name

        val author = dialog.findViewById<TextView>(R.id.AuthorName)
        author.text = mapData.nickname

        author.setOnClickListener {
            val authorIntent = Intent(context, FriendBioActivity::class.java)
            val authId = item.authorId
            Log.d("AUTHORID", authId.toString())
            authorIntent.putExtra("id", authId.toString())
            startActivity(context, authorIntent, null)
        }
        val responseDescr = Requests.GetKnopkaDescription(
            context,
            "http://10.0.2.2:8080/api/v1",
            "111",
            item.id,
            1,
            "knopkaUserId"
        )

        val descriptionDTO = jsonFormat.decodeFromString<Description>(responseDescr)
        Log.d("RES", responseDescr)
        Log.d("ALL)", descriptionDTO.toString())
        Log.d("ALL)", descriptionDTO.tags.toString())

        val knopkaDescriptionText = dialog.findViewById<TextView>(R.id.KnopkaDescriptionText)
        knopkaDescriptionText.setText(descriptionDTO.text)

        val rcView = dialog.findViewById<RecyclerView>(R.id.RecyclerViewTags)
        rcView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcView.adapter = adapter

        val tgs = descriptionDTO.tags
        if (tgs != null) {
            for (t in tgs) {
                adapter.addTag(t)
            }
        }

        dialog.show()
    }

    override fun onItemLongClick(item: String, position: Int) {
//        Log.d("Tag","long click")
        Log.d("in click", position.toString())
        val intent = Intent(context, FeedActivity::class.java)
        intent.putExtra("tag", adapter.tagList[position])
        startActivity(context, intent, null)
    }

    override fun onItemClick(item: String, position: Int) {
        Log.d("in click", position.toString())
        val intent = Intent(context, FeedActivity::class.java)
        intent.putExtra("tag", adapter.tagList[position])
        startActivity(context, intent, null)
    }
}