package com.example.Auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityFollowingBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class FollowingActivity : AppCompatActivity(), OnFriendClickListener {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityFollowingBinding
    lateinit var newList: ArrayList<User>
    lateinit var tmpList: ArrayList<User>
    private val adapter = FriendFeedAdapter(this)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewFriendsFeed.layoutManager =
            LinearLayoutManager(this)
        binding.RecyclerViewFriendsFeed.adapter = adapter
        showUserFriends()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showUserFriends() {
        val friendIdsList = sendGetUserFriendsIds()
        if (friendIdsList?.isNotEmpty() == true) {
            val friendsList = sendGetUserFriends(friendIdsList)
            for (friend in friendsList) {

                adapter.addFriend(User(friend.nickname, friend.bio, friend.photo, friend.id))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

        newList = adapter.friendList
        tmpList = arrayListOf<User>()

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))
        supportActionBar?.title = "Followings"
        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayoutFollowing)
        val navigationView =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navViewFollowing)
        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout?.addDrawerListener(toggle)
        toggle.syncState()


        navigationView.setNavigationItemSelectedListener {
            val switcher = WindowSwitcher(it, this)
            switcher.set()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        val searchItem = menu!!.findItem(R.id.searchKnopkaIcon)
        val searchView = searchItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tmpList.clear()
                val searchText = p0?.toLowerCase(Locale.getDefault())
                if (searchText!!.isNotEmpty()) {
                    newList.forEach {
                        if (it.nickname.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tmpList.add(it)
                        }
                    }
                    adapter.friendList = tmpList
                    adapter.notifyDataSetChanged()
                } else {
                    tmpList.clear()
                    tmpList.addAll(newList)
                    adapter.friendList = tmpList
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item) == true) { //user clicked on toggle button
            return true
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserFriendsIds(): List<Long>? {
        val result = Requests.GetUserFriendsIds(
            ThisUser.userInfo.id
        )

        if (result.length > 2) {
            val friendIdsList =
                jsonFormat.decodeFromString<List<Long>>(result)
            return friendIdsList
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserFriends(friendIdsList: List<Long>): List<User> {
        val result = Requests.GetUserFriends(
            friendIdsList
        )
        val friendsList = Converters.stringToUsers(result.toString())
        newList = friendsList as ArrayList<User>
        return friendsList
    }

    override fun onItemClick(item: User, position: Int) {

        val intent2 = Intent(this, FriendBioActivity::class.java)

        intent2.putExtra("id", item.id.toString())
        startActivity(intent2)
    }
}
