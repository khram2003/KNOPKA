package com.example.Auth


import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.Converters.base64StringToBitMap
import com.example.Auth.Converters.bitMapToBase64String
import com.example.Auth.Converters.stringToButtons
import com.example.Auth.Requests.GetUserProfileInfo
import com.example.Auth.databinding.ActivityBioBinding
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import android.location.Geocoder
import com.example.Auth.RequestCodes.GET_PERMISSIONS_REQUEST_CODE


private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class BioActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityBioBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog

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

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        ThisUser.userInfo = UserInfo()
        setCalendar()

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
            val switcher = WindowSwitcher(it, this)
            switcher.set()
            true
        }

        units.changeInfoButton = findViewById(R.id.changeInfoButton)
        units.textViewName = findViewById(R.id.textViewName)
        units.textViewBio = findViewById(R.id.textViewBio)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePic)

        units.profilePicBitMap =
            BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap

        units.token = intent.getStringExtra("token")

        getCurrentLocation()

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

                            val textViewLabel = mapData["name"].toString()
                            val descriptionCls =
                                Json.decodeFromString<Description>(mapData["descr"]!!)
                            val textViewDescr = descriptionCls.text
                            val tagsList = descriptionCls.tags?.map { x -> '"' + x + '"' }

                            val m: Map<String, String?> = mapOf(
                                "\"name\"" to "\"" + mapData["name"] + "\"",
                                "\"style\"" to /*"\"" + mapData["style"].toString() + "\""*/ null,
                                "\"pushes\"" to "0",
                                "\"id\"" to units.id.toString()
                            )

                            val res = sendPostButtonRequest(
                                m as Map<String, String>
                            ) // the end of the knpokas list

                            val knopka =
                                adapter.addKnopka(
                                    Knopka(
                                        textViewLabel,
                                        "",
                                        0,
                                        res,
                                        ThisUser.userInfo.id
                                    )
                                )

                            val mDiscr: Map<String, Any?> = mapOf(
                                "\"text\"" to "\"" + textViewDescr + "\"",
                                "\"image\"" to null,
                                "\"tags\"" to tagsList
                            )


                            val res1 =
                                sendPostDescriptionRequest(mDiscr, knopka, res)
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
        val result = GetUserProfileInfo(
            ThisUser.userInfo.id
        )
        val mapData: User = jsonFormat.decodeFromString(result)
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
            Requests.PostKnopkaRequest(
                ThisUser.userInfo.id,
                param
            )
        return result.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPostDescriptionRequest(
        param: Map<String, Any?>,
        knopka: KnopkaFeedAdapter,
        idBtn: Long
    ): String {
        val result =
            Requests.PutDescriptionRequest(
                this,
                idBtn,
                param
            )
        return result
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sendPutChangeInfoRequest(param: Map<String, String>) =
        Requests.PutChangeInfoRequest(
            this,
            ThisUser.userInfo.id,
            param
        )

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserKnopkaIds(): List<Long> {
        val result =
            Requests.GetUserKnopkaIds(
                this,
                ThisUser.userInfo.id,
            )

        val knopkaIdsList =
            jsonFormat.decodeFromString<List<Long>>(result)
        return knopkaIdsList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserKnopkas(knopkasIdList: List<Long>): List<Knopka> {
        val result =
            Requests.GetUserKnopkas(
                knopkasIdList
            )
        val knopkasList = stringToButtons(result)
        return knopkasList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showUserKnopkas() {
        val knopkaIdsList = sendGetUserKnopkaIds()
        if (knopkaIdsList.isNotEmpty()) {
            val knopkasList = sendGetUserKnopkas(knopkaIdsList)

            for (knopka in knopkasList) {
                adapter.addKnopka(
                    Knopka(
                        knopka.name,
                        knopka.style,
                        knopka.pushes,
                        knopka.id,
                        knopka.authorId
                    )
                )
            }
        }
    }


    // long click on knopka
    override fun onItemLongClick(item: Knopka, position: Int) {
        val presenter = ShowDescription()
        presenter.showDescription(dialog, item, this)
    }

    override fun onItemClick(item: Knopka, position: Int) {
        if (!CurBatch.working) {
            setCalendar()
        }
        Log.d("AAA", "REGISTERED SHORT CLICK")
        item.pushes++
        CurBatch.setClicks(item.id)
        adapter.notifyDataSetChanged()
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

        return true
    }

    fun setCalendar() {
        CurBatch.working = true
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)

        val myIntent = Intent(this@BioActivity, BatchReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@BioActivity, 0, myIntent, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC, calendar.getTimeInMillis()] = pendingIntent


        if (BatchesToAdd.clicks.isNotEmpty()) {
            for (i in BatchesToAdd.clicks) {
                addBatchToStorage(i)
            }
        }

    }

    fun addBatchToStorage(batch: Batch) {
        val sharedPref = getSharedPreferences("mypref1", 0)
        val editor = sharedPref.edit()

        val batches: List<Batch> =
            jsonFormat.decodeFromString(sharedPref.getString("batches", "").toString())

        removeBatchFromStorage()

        batches.plus(batch);
        editor.putString("batches", jsonFormat.encodeToString(batches))
        editor.apply()
    }

    fun removeBatchFromStorage() {
        val sharedPref = getSharedPreferences("mypref1", 0)
        val editor = sharedPref.edit()

        editor.putString("batches", jsonFormat.encodeToString(BatchesToAdd.clicks))
        editor.apply()
    }


    fun getCurrentLocation() {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermission()) {
            if (isLocationEnabled()) {
                locationProvider.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        //world
                    } else {
                        ThisUser.userInfo.location =
                            getAddress(location.latitude, location.longitude)
                        Log.d("D", ThisUser.userInfo.location)
                    }

                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            GET_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GET_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Log.d("PROBLEM DETECTED", "A")
            }
        }
    }

    fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(lat, lng, 1)
        return addresses[0].countryName
    }


}