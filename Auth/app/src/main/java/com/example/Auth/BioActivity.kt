package com.example.Auth

import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import com.example.Auth.Requests.TESTcLICK
import com.example.Auth.databinding.ActivityBioBinding
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.threeten.bp.DateTimeUtils.toLocalTime
import org.threeten.bp.LocalTime
import java.util.*
import kotlin.collections.set


private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class BioActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityBioBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog
//    val br: BatchReceiver = BatchReceiver
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

        PutAddFriendRequest(this, "http://10.0.2.2:8080/api/v1/user", 1, "111", 2)
        PutAddFriendRequest(this, "http://10.0.2.2:8080/api/v1/user", 1, "111", 3)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidThreeTen.init(this)
        setCalendar()

        super.onCreate(savedInstanceState)

        binding = ActivityBioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        dialog = Dialog(this)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navView)
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

        units.token = intent.getStringExtra("token")


        storageInfoLoad()
//        sendGetUserProfileInfo()
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
                            val textViewDescr = mapData["descr"].toString()

                            //TODO
                            val knopka =
                                adapter.addKnopka(
                                    Knopka(
                                        textViewLabel,
                                        "",
                                        0,
                                        0,
                                        1
                                    )
                                ) // 1 = this userID

                            val m: Map<String, String?> = mapOf(
                                "\"name\"" to "\"" + mapData["name"] + "\"",
                                "\"style\"" to /*"\"" + mapData["style"].toString() + "\""*/ null,
                                "\"pushes\"" to "0",
                                "\"id\"" to units.id.toString()
//                                "\"LocalDateTime\"" to "\"" + mapData["LocalDateTime"] + "\""
                            )

                            sendPostButtonRequest(
                                m as Map<String, String>,
                                knopka,
                                knopka.knopkaList.size - 1
                            ) // the end of the knpokas list

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
    fun sendPostButtonRequest(param: Map<String, String>, knopka: KnopkaFeedAdapter, ind: Int) {
        val result =
            Requests.PostKnopkaRequest(this, "http://10.0.2.2:8080/api/v1/knopka", 1, "111", param)
        if (result != null) {
            knopka.knopkaList[ind].id = result.toLong()
        }
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
                adapter.addKnopka(Knopka(knopka.name, knopka.style, knopka.pushes, knopka.id))
            }
        }
    }


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
        val result =
            GetUserProfileInfo(this, "http://10.0.2.2:8080/api/v1/profile", 1, "111", item.authorID)
        val mapData: User = jsonFormat.decodeFromString(result.toString())
        name.setText(item.name)
//        val result1 = GetKnopksaDescr(
//            this,
//            "localhost:8080/api/v1/description",
//            "111",
//            1,
//            item.id,
//            "knopkaUserId"
//        )
//        Log.d("AAAAAAAAAAAAAAAA", result1)
        //TODO set bio, set author
        val author = dialog.findViewById<TextView>(R.id.AuthorName)
        author.setText(mapData.nickname)
        Log.d("-----", result)
        //TODO
//        val bio = dialog.findViewById<TextView>(R.id.)
        author.setOnClickListener {
            val intent2 = Intent(this, FriendBioActivity::class.java)
            startActivity(intent2)
        }
        dialog.show()
    }

    override fun onItemClick(item: Knopka, position: Int) {
        if(!ClickBatch.working){
            setCalendar()
        }
        Log.d("AAA", "REGISTERED SHORT CLICK")
        item.pushes++
//        curBatch.clicks.put(item.id, 1)
        ClickBatch.setClicks(item.id)
//        TESTcLICK(this)

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

    fun  setCalendar() {
        ClickBatch.working = true
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)

        val myIntent = Intent(this@BioActivity, BatchReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@BioActivity, 0, myIntent, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC, calendar.getTimeInMillis()] = pendingIntent

        Log.d("CAL", calendar.toString())
    }


}