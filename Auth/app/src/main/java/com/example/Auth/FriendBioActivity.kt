package com.example.Auth


import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityFriendBioBinding
import com.example.Auth.Converters.base64StringToBitMap
import com.example.Auth.Converters.stringToButtons
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class FriendBioActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityFriendBioBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog

    class MainActivityUnits {
        var textViewName: TextView? = null
        var textViewBio: TextView? = null
        var imageViewProfilePic: ImageView? = null
        var profilePicBitMap: Bitmap? = null
        var token: String? = null
        var id: Long = 1
    }

    private var units: MainActivityUnits = MainActivityUnits()

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
            LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter
        showUserKnopkas()
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFriendBioBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main1) // same as next line but next line allows to use binding
        setContentView(binding.root)

        units.id = intent.getStringExtra("id")!!.toLong()

        initRecyclerView()

        dialog = Dialog(this)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))
        supportActionBar?.title = ""

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayoutFriend)
        val navigationView =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navViewFriend)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()


        navigationView.setNavigationItemSelectedListener {
            val switcher = WindowSwitcher(it, this)
            switcher.set()
            true
        }

        units.textViewName = findViewById(R.id.textViewNameFriend)
        units.textViewBio = findViewById(R.id.textViewBioFriend)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePicFriend)

        units.profilePicBitMap =
            BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap

        units.token = intent.getStringExtra("token")


        sendGetUserProfileInfo()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    //
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGetUserProfileInfo() {
        val result =
            Requests.GetUserProfileInfo(
                units.id
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
    fun sendGetUserKnopkaIds(): List<Long> {
        val result =
            Requests.GetUserKnopkaIds(this, units.id)

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

        val knopkaIdsList = stringToButtons(result)
        return knopkaIdsList
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_friend_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item) == true) { //user clicked on toggle button
            return true
        }
        when (item.itemId) {
            R.id.addFriendIcon -> {
                Requests.PutAddFriendRequest(
                    this,
                    units.id
                )
            }
        }
        return true
    }

    override fun onItemLongClick(item: Knopka, position: Int) {
        val presenter = ShowDescription()
        presenter.showDescription(dialog, item, this)
    }

    override fun onItemClick(item: Knopka, position: Int) {
        if (!CurBatch.working) {
            setCalendar()
        }

        item.pushes++
        CurBatch.setClicks(item.id)
        adapter.notifyDataSetChanged()
    }

    fun setCalendar() {
        CurBatch.working = true
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)

        val myIntent = Intent(this@FriendBioActivity, BatchReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@FriendBioActivity, 0, myIntent, 0)

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
        batches.plus(batch);
        editor.putString("batches", jsonFormat.encodeToString(batches))
        editor.apply()
    }

    fun removeBatchesFromStorage() {
        val sharedPref = getSharedPreferences("mypref1", 0)
        val editor = sharedPref.edit()

        editor.putString("batches", jsonFormat.encodeToString(BatchesToAdd.clicks))
        editor.apply()
    }
}