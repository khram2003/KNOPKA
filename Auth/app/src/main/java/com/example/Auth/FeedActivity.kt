package com.example.Auth

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.Requests.GetKnopkasByTag
import com.example.Auth.Requests.GetUserKnopkas
import com.example.Auth.databinding.ActivityFeedBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.ArrayList

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class FeedActivity : AppCompatActivity(), OnKnopkaClickListener {
    lateinit var binding: ActivityFeedBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var newList: ArrayList<Knopka>
    lateinit var tmpList: ArrayList<Knopka>

    private val adapter = KnopkaFeedAdapter(this)
    lateinit var dialog: Dialog

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewKnopkasFeed.layoutManager =
            LinearLayoutManager(this)
        binding.RecyclerViewKnopkasFeed.adapter = adapter

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

        newList = adapter.knopkaList
        tmpList = arrayListOf<Knopka>()

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))
        supportActionBar?.title = "Knopkas"

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayoutFeed)
        val navigationView =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navViewFeed)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()


        navigationView.setNavigationItemSelectedListener {
            val switcherSetter = WindowSwitcherSetter("Feed", it, this, dLayout, navigationView)
            switcherSetter.set()
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showAllKnopkas() {
        val knopkasList = sendGetAllKnopkas()
        Log.d("----------", knopkasList.toString())
        if (knopkasList.isNotEmpty()) {
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetAllKnopkas(): List<Knopka> {
        val result =
            Requests.GetAllKnopkas(this, "http://10.0.2.2:8080/api/v1/knopka", 1, "111")
        Log.d("KNOPKAS", result.toString())
        val knopkasList = jsonFormat.decodeFromString<List<Knopka>>(result)
        newList = knopkasList as ArrayList<Knopka>
        return knopkasList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showKnopkasByTag(p0: String?) {
        tmpList.clear()
        val searchText = p0?.toLowerCase(Locale.getDefault())
        if (searchText!!.isNotEmpty()) {
            val result = GetKnopkasByTag(
                "http://10.0.2.2:8080/api/v1", "111",
                searchText, 1, "tag", "knopkaUserId"
            ) // todo knopkauserId 1
            val foundKnopkasIds = jsonFormat.decodeFromString<List<Long>>(result)
            Log.d("tagseerch", foundKnopkasIds.toString())
            tmpList = jsonFormat.decodeFromString(
                GetUserKnopkas(
                    null, "http://10.0.2.2:8080/api/v1/knopka",
                    1, /*todo not 1*/
                    "111",
                    foundKnopkasIds
                )
            )
            Log.d("knopkasFound", tmpList.toString())
            adapter.knopkaList = tmpList
            adapter.notifyDataSetChanged()
        } else {
            tmpList.clear()
            tmpList.addAll(newList)
            adapter.knopkaList = tmpList
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemLongClick(item: Knopka, position: Int) {
        val presenter = ShowDescription()
        presenter.showDescription(dialog, item, this)
    }

    override fun onItemClick(item: Knopka, position: Int) {
        Log.d("FEED", "REGISTERED SHORT CLICK")
        item.pushes++
        Toast.makeText(this, item.pushes.toString(), Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        val searchItem = menu!!.findItem(R.id.searchKnopkaIcon)
        val searchView = searchItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                showKnopkasByTag(p0)
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item) == true) { //user clicked on toggle button
            return true
        }
//        when (item.itemId) {
//            R.id.searchKnopkaIcon -> {
//                //todo
//            }
//        }
        return true
    }
}