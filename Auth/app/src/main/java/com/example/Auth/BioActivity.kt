package com.example.Auth


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.Auth.Converters.base64StringToBitMap
import com.example.Auth.Converters.bitMapToBase64String
import com.example.Auth.Converters.stringToButtons
import com.example.Auth.Requests.GetUserProfileInfo
import com.example.Auth.Requests.PutAddFriendRequest
import com.example.Auth.databinding.ActivityBioBinding
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
                LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter

        showUserKnopkas()
        for (k in adapter.knopkaList) {
            Log.d("--------------------", k.toString())

        }

        PutAddFriendRequest(this, "http://10.0.2.2:8080/api/v1/user", 1, "111", 2)
        PutAddFriendRequest(this, "http://10.0.2.2:8080/api/v1/user", 1, "111", 3)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        dialog = Dialog(this)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))
        supportActionBar?.title = "My Profile"

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView =
                findViewById<com.google.android.material.navigation.NavigationView>(R.id.navView)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()

        //items clicked on slideout menu
        navigationView.setNavigationItemSelectedListener {
            val switcherSetter = WindowSwitcherSetter("Bio", it, this, dLayout, navigationView)
            switcherSetter.set()
            true
        }

        units.changeInfoButton = findViewById(R.id.changeInfoButton)
        units.textViewName = findViewById(R.id.textViewName)
        units.textViewBio = findViewById(R.id.textViewBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePic)

        units.profilePicBitMap =
                BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap

        units.token = intent.getStringExtra("token")


        storageInfoLoad()
        sendGetUserProfileInfo()
        storageInfoUpdate()



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
                            val descriptionCls = Json.decodeFromString<Description>(mapData["descr"]!!)
                            val textViewDescr = descriptionCls.text
                            val tagsList = descriptionCls.tags?.map{x -> '"'+ x + '"'}
                            Log.d("LISTTAGS", tagsList.toString())

                            val m: Map<String, String?> = mapOf(
                                    "\"name\"" to "\"" + mapData["name"] + "\"",
                                    "\"style\"" to /*"\"" + mapData["style"].toString() + "\""*/ null,
                                    "\"pushes\"" to "0",
                                    "\"id\"" to units.id.toString()
//                                "\"LocalDateTime\"" to "\"" + mapData["LocalDateTime"] + "\""
                            )

                            val res = sendPostButtonRequest(
                                    m as Map<String, String>
                            ) // the end of the knpokas list

                            //TODO
                            val knopka =
                                    adapter.addKnopka(Knopka(textViewLabel, "", 0, res, 1)) // 1 = this userID

                            val mDiscr: Map<String, Any?> = mapOf(
                                    "\"text\"" to "\"" + textViewDescr + "\"",
                                    "\"image\"" to null,
                                    "\"tags\"" to tagsList
                            )

                            Log.d("MAPDISCR", mDiscr.toString())

                            val res1 = sendPostDescriptionRequest(mDiscr as Map<String, Any?>, knopka, res)
                            Log.d("RESPONSEPUTDISCR", res1.toString())
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGetUserProfileInfo() {
        val result = GetUserProfileInfo(this, "http://10.0.2.2:8080/api/v1/profile", 1, "111", 1)
        val mapData: User = jsonFormat.decodeFromString(result.toString())
        units.textViewName?.text = mapData.nickname
        units.textViewBio?.text = mapData.bio
        if (mapData.photo != "") {
            units.profilePicBitMap =
                    base64StringToBitMap(mapData.photo.substring(0, mapData.photo.length - 2))
            units.imageViewProfilePic?.setImageBitmap(units.profilePicBitMap)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPostButtonRequest(param: Map<String, String>): Long {
        val result =
                Requests.PostKnopkaRequest(this, "http://10.0.2.2:8080/api/v1/knopka", 1, "111", param)
        return result.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPostDescriptionRequest(param: Map<String, Any?>, knopka: KnopkaFeedAdapter, idBtn: Long) : String {
        val result =
                Requests.PutDescriptionRequest(this, "http://10.0.2.2:8080/api/v1/description", idBtn, "111", param)
        return result
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPutChangeInfoRequest(param: Map<String, String>) =
            Requests.PutChangeInfoRequest(this, "http://10.0.2.2:8080/api/v1/profile", 1, "111", param)

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserKnopkaIds(): List<Long> {
        val result =
                Requests.GetUserKnopkaIds(this, "http://10.0.2.2:8080/api/v1/user", 1, 1, "111")
        Log.d("KNOKA IDS", result.toString())
        val knopkaIdsList =
                jsonFormat.decodeFromString<List<Long>>(result)
        return knopkaIdsList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserKnopkas(knopkasIdList: List<Long>): List<Knopka> {
        val result =
                Requests.GetUserKnopkas(
                        this,
                        "http://10.0.2.2:8080/api/v1/knopka",
                        1,
                        "111",
                        knopkasIdList
                )
        Log.d("KNOPKAS", result.toString())
        val knopkasList = stringToButtons(result.toString())
        return knopkasList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showUserKnopkas() {
        val knopkaIdsList = sendGetUserKnopkaIds()
        if (knopkaIdsList.isNotEmpty()) {
            val knopkasList = sendGetUserKnopkas(knopkaIdsList)

            for (knopka in knopkasList) {
                Log.d("showUserKnopkasIN", knopka.toString())
                adapter.addKnopka(Knopka(knopka.name, knopka.style, knopka.pushes, knopka.id, knopka.authorId))
            }
        }
    }


    // long click on knopka
    override fun onItemLongClick(item: Knopka, position: Int) {

//        dialog.setContentView(R.layout.activity_pop_up_info)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val name = dialog.findViewById<TextView>(R.id.KnopkaName)
//        val userProfileInfo = GetUserProfileInfo(this, "http://10.0.2.2:8080/api/v1/profile", 1, "111", 1) //TODO NOT 1
//        val mapData: User = jsonFormat.decodeFromString(userProfileInfo.toString())
//        name.setText(item.name)
//
//        val author = dialog.findViewById<TextView>(R.id.AuthorName)
//        author.setText(mapData.nickname)
//
//        author.setOnClickListener {
//            val authorIntent = Intent(this, FriendBioActivity::class.java)
//            val authId = item.authorId
//            Log.d("AUTHORID", authId.toString())
//            authorIntent.putExtra("id", authId.toString())
//            startActivity(authorIntent)
//        }
//        val knopkaDescr = GetKnopksaDescr(this, "http://10.0.2.2:8080/api/v1", "111", item.id, 1, "knopkaUserId")
//
//        val descriptionText = jsonFormat.decodeFromString<Description>(knopkaDescr)
//        Log.d("RES", knopkaDescr)
//        Log.d("ALL)", descriptionText.toString())
//        val discriptionText = dialog.findViewById<TextView>(R.id.KnopkaDescriptionText)
//        discriptionText.setText(descriptionText.text)
//        dialog.show()
        val presenter = ShowDescription(dialog, item, this)
        presenter.showDescription()
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
        val toolbarItemSetter = ToolbarItemSetter("Bio", item, this)
        toolbarItemSetter.set()
        Log.d("BIOTOOLBARSETTED", "Setted Icons")
        return true
    }

}