package com.example.Auth


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.*

const val CHANGE_INFO_REQUEST_CODE = 100


class BioActivity : AppCompatActivity() {

    class MainActivityUnits {

        var changeInfoButton: Button? = null
        var textViewName: TextView? = null
        var textViewBio: TextView? = null
        var imageViewProfilePic: ImageView? = null
        var profilePicBitMap: Bitmap? = null // Profile picture bitmap
        var autPassed: Boolean = false
    }

    private var units: MainActivityUnits = MainActivityUnits()

    @RequiresApi(Build.VERSION_CODES.O)
    fun bitMapToBase64String(bitMap: Bitmap?): String? {
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio)

        units.changeInfoButton = findViewById(R.id.changeInfoButton)
        units.textViewName = findViewById(R.id.textViewName)
        units.textViewBio = findViewById(R.id.textViewBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePic)

        units.profilePicBitMap =
            BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap

        if (!storageInfoLoad()) {
            // start authentication intent
            units.autPassed = true
            storageInfoUpdate()
        }

        units.changeInfoButton?.setOnClickListener { //change info button listener

            val imageString: String? = bitMapToBase64String(units.profilePicBitMap)
            val nameString: String = units.textViewName?.text.toString()
            val bioString: String = units.textViewBio?.text.toString()

            val mapData = // create json transfer object
                mapOf(
                    "name" to nameString,
                    "bio" to bioString,
                    "pic" to imageString
                )

            val jsonData = Json.encodeToString(mapData)

            val intent1 = Intent(this, ChangeInfoActivity::class.java).apply {
                putExtra("data", jsonData)
            }

            startActivityForResult(intent1, CHANGE_INFO_REQUEST_CODE)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    CHANGE_INFO_REQUEST_CODE -> {
                        val jsonData = data.getStringExtra("data")
                        val mapData: Map<String, String>
                        if (jsonData != null) {
                            mapData = Json.decodeFromString(jsonData)
                            units.textViewName?.text = mapData["name"] // set with new values
                            units.textViewBio?.text = mapData["bio"]
                            val stringImage = base64StringToBitMap(mapData["pic"])
                            units.imageViewProfilePic?.setImageBitmap(stringImage)
                            units.profilePicBitMap = stringImage

                            storageInfoUpdate()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storageInfoUpdate()/*: Boolean*/ {

        val imageString: String? = bitMapToBase64String(units.profilePicBitMap)
        val nameString: String = units.textViewName?.text.toString()
        val bioString: String = units.textViewBio?.text.toString()
        val autPassedString: String = units.autPassed.toString()

        val sharedPref = getSharedPreferences("mypref", 0)
        val editor = sharedPref.edit()


        editor.putString("name", nameString)
        editor.putString("bio", bioString)
        editor.putString("image", imageString)
        editor.putString("aut", autPassedString)

        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storageInfoLoad(): Boolean {

        val sharedPref = getSharedPreferences("mypref", 0)

        val nameString = sharedPref.getString("name", "").toString()
        val bioString = sharedPref.getString("bio", "").toString()
        val imageString = sharedPref.getString("image", "").toString()
        val autPassedString = sharedPref.getString("aut", "").toString()

        units.profilePicBitMap = base64StringToBitMap(imageString)
        units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
        units.textViewName?.text = nameString
        units.textViewBio?.text = bioString
        units.autPassed = autPassedString == "true"

        return units.autPassed
    }

}