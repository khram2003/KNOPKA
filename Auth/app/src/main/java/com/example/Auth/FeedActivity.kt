package com.example.Auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityBioBinding
import com.example.Auth.databinding.ActivityFeedBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class FeedActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityFeedBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
                LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter
//        adapter.addKnopka(Knopka("Btn1"))
//        adapter.addKnopka(Knopka("Btn2"))
//        adapter.addKnopka(Knopka("Btn3"))
//        adapter.addKnopka(Knopka("Btn1"))
//        adapter.addKnopka(Knopka("Btn2"))
//        adapter.addKnopka(Knopka("Btn3"))

        showAllKnopkas()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_feed)
        setContentView(binding.root)
        initRecyclerView()

        dialog = Dialog(this)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayoutFeed)
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navViewFeed)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feed -> dLayout.closeDrawer(navigationView)
                R.id.following -> {
                    val intent2 = Intent(this, FollowingActivity::class.java)
                    startActivity(intent2)
                }
                R.id.myProfile -> {
                    val intent2 = Intent(this, BioActivity::class.java)
                    startActivity(intent2)
                }
            }

            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showAllKnopkas() {
        val knopkasList = sendGetAllKnopkas()
        Log.d("----------", knopkasList.toString())
        if (knopkasList.isNotEmpty()) {
            for (knopka in knopkasList) {
                adapter.addKnopka(Knopka(knopka.name, knopka.style, knopka.pushes, knopka.id))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetAllKnopkas(): List<Knopka> {
        val result =
                Requests.GetAllKnopkaIds(this, "http://10.0.2.2:8080/api/v1/knopka", 1, "111")
        Log.d("KNOKAS", result.toString())
        val knopkasList =
                jsonFormat.decodeFromString<List<Knopka>>(result)
        return knopkasList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item) == true) { //user clicked on toggle button
            return true
        }
        return true
    }

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
        val author = dialog.findViewById<TextView>(R.id.AuthorName)
//        author.setText(item.)
        //TODO return back to feed
        author.setOnClickListener {
            val intent2 = Intent(this, FriendBioActivity::class.java)
            startActivity(intent2)
        }
        dialog.show()
    }

    override fun onItemClick(item: Knopka, position: Int) {
        Log.d("FEED", "REGISTERED SHORT CLICK")
        item.pushes++
        Toast.makeText(this, item.pushes.toString(), Toast.LENGTH_SHORT).show()
    }
}