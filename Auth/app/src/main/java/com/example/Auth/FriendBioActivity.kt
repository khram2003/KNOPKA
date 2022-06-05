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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.serialization.json.Json

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
            val switcherSetter = WindowSwitcherSetter("Bio", it, this, dLayout, navigationView)
            switcherSetter.set()
            true
        }

        units.textViewName = findViewById(R.id.textViewNameFriend)
        units.textViewBio = findViewById(R.id.textViewBioFriend)
        units.imageViewProfilePic = findViewById(R.id.imageViewProfilePicFriend)

        units.profilePicBitMap =
            BitmapFactory.decodeResource(resources, R.drawable.img) //get default picture bitmap
//        units.googleLogOutButton = findViewById(R.id.googleLogOutButton)

        units.token = intent.getStringExtra("token")


        sendGetUserProfileInfo()

        // sendGetUserFriendsList() //TODO

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
                this, "http://10.0.2.2:8080/api/v1/profile",
                1,
                "111",
                units.id
            )
        val mapData: User = jsonFormat.decodeFromString(result.toString())
        Log.d("AAAAAAAAAAAAAAAAAAA", mapData.toString())
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
            Requests.GetUserKnopkaIds(this, "http://10.0.2.2:8080/api/v1/user", units.id, 1, "111")
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
        val knopkaIdsList = stringToButtons(result.toString())
        return knopkaIdsList
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun sendGetUserFriendsList() = getUserFriendsList().execute()

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
                //TODO
                Requests.PutAddFriendRequest(
                    this,
                    "http://10.0.2.2:8080/api/v1/user",
                    1,
                    "111",
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
        Log.d("AAA", "REGISTERED SHORT CLICK")
        item.pushes++
        Toast.makeText(this, item.pushes.toString(), Toast.LENGTH_SHORT).show()
    }
}