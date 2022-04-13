package com.example.Auth

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException


class BioActivity : AppCompatActivity() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio)

        Log.d("aaa", intent.getStringExtra("token").toString())

        val changeInfoButton = findViewById<Button>(R.id.changeInfoButton)
        changeInfoButton.setOnClickListener { //nickname change listener
            val textViewName = findViewById<TextView>(R.id.textViewName)
            val textViewBio = findViewById<TextView>(R.id.textViewBio)
            val mapData =
                mapOf("name" to textViewName.text.toString(), "bio" to textViewBio.text.toString())
            val jsonData = Json.encodeToString(mapData)
            val intent1 = Intent(this, ChangeInfoActivity::class.java).apply {
                putExtra("data", jsonData)
            }
            startActivityForResult(intent1, 1)
        }

    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                1 -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val jsonData = data.getStringExtra("data")
                        val mapData: Map<String, String>
                        if (jsonData != null) {
                            mapData = Json.decodeFromString(jsonData)

                            val textViewName = findViewById<TextView>(R.id.textViewName)
                            textViewName.text = mapData["name"]
                            val textViewBio = findViewById<TextView>(R.id.textViewBio)
                            textViewBio.text = mapData["bio"]

                        }
                    }
                }
//                2 -> {
//                    if(resultCode == Activity.RESULT_OK){
//                        val selectedImage: Uri = attr.data.getData()
//                        var bitmap: Bitmap? = null
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(
//                                this.contentResolver,
//                                selectedImage
//                            )
//                        } catch (e: FileNotFoundException) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace()
//                        } catch (e: IOException) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace()
//                        }
//                    }
//                }
            }
        }
    }
}