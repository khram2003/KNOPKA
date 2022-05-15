package com.example.Auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.ByteArrayOutputStream
import java.lang.Short.decode
import java.nio.charset.StandardCharsets
import java.util.Base64



class ChangeInfoActivity : AppCompatActivity() {

    class ChangeInfoActivityUnits {
        var applyButton: Button? = null
        var cancelButton: Button? = null
        var editTextName: TextView? = null
        var editTextBio: TextView? = null
        var imageViewProfilePic: ImageView? = null
        var profilePicBitMap: Bitmap? = null // Profile picture bitmap
    }

    private var units: ChangeInfoActivityUnits = ChangeInfoActivityUnits()

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
        setContentView(R.layout.activity_change_info)

        units.applyButton = findViewById(R.id.applyButton)
        units.cancelButton = findViewById(R.id.cancelButton)
        units.editTextName = findViewById(R.id.editTextName)
        units.editTextBio = findViewById(R.id.editTextBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePicSetter)


        val jsonDataMain = intent.getStringExtra("data")
        val mapDataMain: Map<String, String>

        if (jsonDataMain != null) {
            mapDataMain = Json.decodeFromString(jsonDataMain)
            units.editTextName?.text = mapDataMain["name"] // set with values from main
            units.editTextBio?.text = mapDataMain["bio"]
            units.profilePicBitMap = base64StringToBitMap(mapDataMain["pic"].toString())
            units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
        }



        units.imageViewProfilePic?.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_OPEN_DOCUMENT, // or ACTION_PICK?
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI // or EXTERNAL_CONTENT_URI?
                ).also {
                    it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    it.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    it.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                },RequestCodes.PICK_PHOTO_REQUEST_CODE
            )
        }


        units.applyButton?.setOnClickListener { // nickname change listener

            val imageString: String? = bitMapToBase64String(units.profilePicBitMap)
            val nameString: String = units.editTextName?.text.toString()
            val bioString: String = units.editTextBio?.text.toString()

//            Toast.makeText(this, name, Toast.LENGTH_SHORT).show() //show new value at the bottom

            val returnIntent = Intent()
            val mapData = mapOf("name" to nameString, "bio" to bioString, "pic" to imageString)
            val jsonData = Json.encodeToString(mapData)
            returnIntent.putExtra("data", jsonData) //return new name from activity
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        units.cancelButton?.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                   RequestCodes.PICK_PHOTO_REQUEST_CODE -> {
                        val picUri = data.data
                        val picBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, picUri)
                        units.imageViewProfilePic?.setImageBitmap(picBitmap)
                        units.profilePicBitMap = picBitmap
                    }
                }
            }
        }
    }

}