package com.example.Auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.Auth.R
import kotlinx.serialization.*
import kotlinx.serialization.json.*


class ChangeInfoActivity : AppCompatActivity() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)
        val jsonDataMain = intent.getStringExtra("data")
        val mapDataMain: Map<String, String>
        if (jsonDataMain != null) {
            mapDataMain = Json.decodeFromString(jsonDataMain)
            val nameEditText = findViewById<TextView>(R.id.editTextName)
            val bioEditText = findViewById<TextView>(R.id.editTextBio)
            nameEditText.text = mapDataMain["name"]
            bioEditText.text = mapDataMain["bio"]
        }

        val image =  findViewById<ImageView>(R.id.profilePicSetter)

        image.setOnClickListener {
//            image.setImageResource(R.drawable.img_1);
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), 2
            )
        }

        val applyButton = findViewById<Button>(R.id.applyButton)
        applyButton.setOnClickListener { //nickname change listener
            val nameEditText = findViewById<EditText>(R.id.editTextName)
            val name = nameEditText.text.toString()
            val bioEditText = findViewById<EditText>(R.id.editTextBio)
            val bio = bioEditText.text.toString()
//            Toast.makeText(this, name, Toast.LENGTH_SHORT).show() //show new value at the bottom

            val returnIntent = Intent()
            val mapData = mapOf("name" to name, "bio" to bio)
            val jsonData = Json.encodeToString(mapData)
            returnIntent.putExtra("data", jsonData) //return new name from activity
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}