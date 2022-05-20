package com.example.Auth


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityBioBinding
import kotlinx.android.synthetic.main.activity_bio.*
import kotlinx.android.synthetic.main.activity_pop_up_info.view.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.*

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class BioActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityBioBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog
//    private var ind: Int = 0;

    class MainActivityUnits {

        var changeInfoButton: Button? = null
        var textViewName: TextView? = null
        var textViewBio: TextView? = null
        var imageViewProfilePic: ImageView? = null
        var profilePicBitMap: Bitmap? = null
        var token: String? = null
        var id: Int = 1
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

    fun stringToButtons(knopkasString: String): List<Knopka> {
        val knopkaIdsList =
                knopkasString.removeSurrounding("[", "]")
                        .split("(?<=\\}),".toRegex()).map { jsonFormat.decodeFromString<Knopka>(it) }

        return knopkaIdsList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
                LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter

//        sendGetUserKnopkaIds()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBioBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main1) // same as next line but next line allows to use binding
        setContentView(binding.root)
        initRecyclerView()

        dialog = Dialog(this)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navView)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()

        //items clicked on slideout menu
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.feed -> {
                    val intent2 = Intent(this, FeedActivity::class.java)
                    startActivity(intent2)
                }

                R.id.following -> {
                    val intent2 = Intent(this, FollowingActivity::class.java)
                    startActivity(intent2)
                }

                R.id.myProfile -> dLayout.closeDrawer(navigationView)
            }

            true
        }

        units.changeInfoButton = findViewById(R.id.changeInfoButton)
        units.textViewName = findViewById(R.id.textViewName)
        units.textViewBio = findViewById(R.id.textViewBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePic)

        units.profilePicBitMap =
                BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap
//        units.googleLogOutButton = findViewById(R.id.googleLogOutButton)

        units.token = intent.getStringExtra("token")


        storageInfoLoad()
//        sendGetUserProfileInfo()
        storageInfoUpdate()


        // sendGetUserFriendsList() //TODO


        binding.changeInfoButton.setOnClickListener {

            val imageString: String? = bitMapToBase64String(units.profilePicBitMap)
            val nameString: String = units.textViewName?.text.toString()
            val bioString: String = units.textViewBio?.text.toString()

            val mapData =
                    mapOf(
                            "nickname" to nameString,
                            "bio" to bioString,
                            "photo" to imageString
                    )

            val jsonData = Json.encodeToString(mapData)

            val intent1 = Intent(this, ChangeInfoActivity::class.java).apply {
                putExtra("data", jsonData)
            }

            startActivityForResult(intent1, RequestCodes.CHANGE_INFO_REQUEST_CODE)
        }


        // не нужна
//        units.googleLogOutButton?.setOnClickListener {
//            val intent2 = Intent(this, ProfileActivity::class.java)
//            storageInfoClear()
//            startActivity(intent2)
//        }

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
                            val param: Map<String, String?> = mapOf(
                                    "\"nickname\"" to
                                            if (mapData["nickname"] == units.textViewName?.text) null
                                            else "\"" + mapData["nickname"] + "\"",
                                    "\"bio\"" to
                                            if (mapData["bio"] == units.textViewBio?.text) null
                                            else "\"" + mapData["bio"].toString() + "\"",
                                    "\"photo\"" to
                                            if (base64StringToBitMap(mapData["photo"]) == units.profilePicBitMap) null
                                            else "\"" + mapData["photo"] + "\""
                            )

                            units.textViewName?.text = mapData["nickname"] // set with new values
                            units.textViewBio?.text = mapData["bio"]
                            val stringImage = base64StringToBitMap(mapData["photo"])
                            units.imageViewProfilePic?.setImageBitmap(stringImage)
                            units.profilePicBitMap = stringImage
                            storageInfoUpdate()

                            sendPutChangeInfoRequest(param as Map<String, String>)
                        }
                    }
                    RequestCodes.CREATE_BUTTON_REQUEST_CODE -> {

                        val jsonData = data.getStringExtra("data")
                        val mapData: Map<String, String>
                        if (jsonData != null) {
                            mapData = Json.decodeFromString(jsonData)
                            Log.d("EXPECTED JSON", mapData.toString())
                            val textViewLabel = mapData["name"].toString()
                            val textViewDescr = mapData["descr"].toString()

                            val knopka =
                                    adapter.addKnopka(Knopka(textViewLabel, "", 0, 0))

                            val m: Map<String, String?> = mapOf(
                                    "\"name\"" to "\"" + mapData["name"] + "\"",
                                    "\"style\"" to /*"\"" + mapData["style"].toString() + "\""*/ null,
                                    "\"pushes\"" to "0",
                                    "\"id\"" to units.id.toString()
//                                "\"LocalDateTime\"" to "\"" + mapData["LocalDateTime"] + "\""
                            )

//                            sendPostButtonRequest(
//                                    m as Map<String, String>,
//                                    knopka,
//                                    knopka.knopkaList.size - 1
//                            ) // the end of the knpokas list
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
        val tokenString: String = units.token.toString()
        val sharedPref = getSharedPreferences("mypref", 0)
        val editor = sharedPref.edit()


        editor.putString("nickname", nameString)
        editor.putString("bio", bioString)
        editor.putString("image", imageString)
        editor.putString("token", tokenString)

        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storageInfoLoad() /*: Boolean*/ {

        val sharedPref = getSharedPreferences("mypref", 0)

        val nameString = sharedPref.getString("nickname", "").toString()
        val bioString = sharedPref.getString("bio", "").toString()
        val imageString = sharedPref.getString("image", "").toString()

        if (base64StringToBitMap(imageString) != null)
            units.profilePicBitMap = base64StringToBitMap(imageString)
        units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
        units.textViewName?.text = nameString
        units.textViewBio?.text = bioString
    }

    private fun storageInfoClear() /*: Boolean*/ {
        val sharedPref = getSharedPreferences("mypref", 0)
        sharedPref.edit().clear().apply()
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun sendGetUserProfileInfo() {
//        val result = Requests.getUserProfileInfo().execute().get()
//        val mapData: User = jsonFormat.decodeFromString(result.toString())
//        units.textViewName?.text = mapData.nickname
//        units.textViewBio?.text = mapData.bio
//        if (mapData.photo != "") {
//            units.profilePicBitMap =
//                    base64StringToBitMap(mapData.photo.substring(0, mapData.photo.length - 2))
//            units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
//        }
//
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPostButtonRequest(param: Map<String, String>, knopka: KnopkaFeedAdapter, ind: Int) {
        val result = Requests.PostButtonRequest(param, knopka, ind).execute().get()
        if (result != null) {
            knopka.knopkaList[ind].id = result.toLong()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPutChangeInfoRequest(param: Map<String, String>) =
            Requests.PutChangeInfoRequest(param).execute()

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun sendGetUserKnopkaIds() {
//        val result = Requests.GetUserKnopkaIds().execute().get()
//        Log.d("KNOKA IDS", result.toString())
//        if (result?.length!! > 2) { // TODO: try???
//            val knopkaIdsList =
//                    result.toString().removeSurrounding("[", "]").split(",").map { it.toLong() }
//            sendGetUserKnopkas(knopkaIdsList)
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserKnopkas(knopkasIdList: List<Long>) {
        val result = GetUserKnopkas(knopkasIdList).execute().get()
        Log.d("KNOPKAS", result.toString())
        val knopkaIdsList = stringToButtons(result.toString())
        for (knopka in knopkaIdsList) {
            adapter.addKnopka(Knopka(knopka.name, knopka.style, knopka.pushes, knopka.id))
        }
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun sendGetUserFriendsList() = getUserFriendsList().execute()

    // long click on knopka
    override fun onItemLongClick(item: Knopka, position: Int) {
//        val popUpActivity = findViewById<RelativeLayout>(R.layout.activity_pop_up_info)
//        val knopkaName = popUpActivity.KnopkaName
//        knopkaName.setText(item.name)
//        val popUpInfo = CustomDialogFragment()
//        popUpInfo.show(supportFragmentManager, "customDialog")
        dialog.setContentView(R.layout.activity_pop_up_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val name = dialog.findViewById<TextView>(R.id.KnopkaName)
        name.setText(item.name)

        //TODO set bio, set author
        val author = dialog.findViewById<TextView>(R.id.AuthorName)
//        author.setText(item.)
        //TODO

        dialog.show()
    }

    override fun onItemClick(item: Knopka, position: Int) {
        Log.d("AAA", "REGISTERED SHORT CLICK")
        item.pushes++
        Toast.makeText(this, item.pushes.toString(), Toast.LENGTH_SHORT).show()
    }


    // toolbar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bio_toolbar_menu, menu)
        return true
    }

    // items from toolbar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item) == true) { //user clicked on toggle button
            return true
        }
        when (item.itemId) {
            R.id.logOutIcon -> {
                val intentLogOut = Intent(this, ProfileActivity::class.java)
                startActivity(intentLogOut)
            }
            R.id.addKnopkaIcon -> {
                val intentCreateKnopka = Intent(this, CreateButtonActivity::class.java)
                startActivityForResult(intentCreateKnopka, RequestCodes.CREATE_BUTTON_REQUEST_CODE)
            }
        }
        return true
    }

}