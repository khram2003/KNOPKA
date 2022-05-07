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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityBioBinding
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.*


class BioActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityBioBinding
    private val adapter = KnopkaFeedAdapter(this)
    private var ind: Int = 0;

    class MainActivityUnits {

        var changeInfoButton: Button? = null
        var textViewName: TextView? = null
        var textViewBio: TextView? = null
        var imageViewProfilePic: ImageView? = null
        var googleLogOutButton: Button? = null
        var profilePicBitMap: Bitmap? = null // Profile picture bitmap

        //        var autPassed: Boolean = false // TODO ???
        var token: String? = null
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

    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
            LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter
        binding.addKnopkaButtton.setOnClickListener {
            val intent2 = Intent(this, CreateButtonActivity::class.java)
            startActivityForResult(intent2, RequestCodes.CREATE_BUTTON_REQUEST_CODE)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBioBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main1) // same as next line but next line allows to use binding
        setContentView(binding.root)
        initRecyclerView()

        units.changeInfoButton = findViewById(R.id.changeInfoButton)
        units.textViewName = findViewById(R.id.textViewName)
        units.textViewBio = findViewById(R.id.textViewBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePic)

        units.profilePicBitMap =
            BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap
        units.googleLogOutButton = findViewById(R.id.googleLogOutButton)
        units.token = intent.getStringExtra("token")

        //TODO: server
        storageInfoLoad()

        if (units.token != null) {
            storageInfoUpdate() // ???
            // load from server
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

            startActivityForResult(intent1, RequestCodes.CHANGE_INFO_REQUEST_CODE)
        }

        units.googleLogOutButton?.setOnClickListener {
            val intent2 = Intent(this, ProfileActivity::class.java)
            clearData()
            startActivity(intent2)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    RequestCodes.CHANGE_INFO_REQUEST_CODE -> {
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
                    RequestCodes.CREATE_BUTTON_REQUEST_CODE -> {

                        val jsonData = data.getStringExtra("data")
                        val mapData: Map<String, String>
                        if (jsonData != null) {
                            mapData = Json.decodeFromString(jsonData)
                            val textViewLabel = mapData["label"].toString()
                            val textViewDescr = mapData["descr"].toString()

                            adapter.addKnopka(Knopka(textViewLabel, textViewDescr, 0))
                            ind++
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
//        val autPassedString: String = units.autPassed.toString() // TODO ???
        val tokenString: String = units.token.toString()
        val sharedPref = getSharedPreferences("mypref", 0)
        val editor = sharedPref.edit()


        editor.putString("name", nameString)
        editor.putString("bio", bioString)
        editor.putString("image", imageString)
//        editor.putString("aut", autPassedString) // TODO ???
        editor.putString("token", tokenString)

        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storageInfoLoad() /*: Boolean*/ {

        val sharedPref = getSharedPreferences("mypref", 0)

        val nameString = sharedPref.getString("name", "").toString()
        val bioString = sharedPref.getString("bio", "").toString()
        val imageString = sharedPref.getString("image", "").toString() //TODO
        val tokenString = sharedPref.getString("token", "").toString()
        if (base64StringToBitMap(imageString) != null)
            units.profilePicBitMap = base64StringToBitMap(imageString)
        units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
        units.textViewName?.text = nameString
        units.textViewBio?.text = bioString
        units.token = tokenString
    }

    fun clearData() /*: Boolean*/ {
        val sharedPref = getSharedPreferences("mypref", 0)
        sharedPref.edit().clear().apply()
    }


    override fun onItemLongClick(item: Knopka, position: Int) {
        Log.d("AAA", "REGISTERED LONG CLICK")
        Toast.makeText(this, item.description, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: Knopka, position: Int) {
        item.timesClicked++
        Toast.makeText(this, item.timesClicked.toString(), Toast.LENGTH_SHORT).show()

    }

}