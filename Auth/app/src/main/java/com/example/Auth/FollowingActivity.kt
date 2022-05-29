package com.example.Auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Auth.databinding.ActivityBioBinding
import com.example.Auth.databinding.ActivityFollowingBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.Arrays.asList

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

class FollowingActivity : AppCompatActivity(), OnFriendClickListener {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityFollowingBinding
    private val adapter = FriendFeedAdapter(this)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        binding.RecyclerViewFriendsFeed.layoutManager =
            LinearLayoutManager(this)
        binding.RecyclerViewFriendsFeed.adapter = adapter
//        adapter.addFriend(User("name1"))
//        adapter.addFriend(User("name2"))
//        adapter.addFriend(User("name3"))
//        adapter.addFriend(User("name4"))

        showUserFriends()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showUserFriends() {
        val friendIdsList = sendGetUserFriendsIds()
        if (friendIdsList?.isNotEmpty() == true) {
            val friendsList = sendGetUserFriends(friendIdsList)
            for (friend in friendsList) {
                Log.d("ID", friend.id.toString())
                adapter.addFriend(User(friend.nickname, friend.bio, friend.photo, friend.id))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowingBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_following)
        setContentView(binding.root)
        initRecyclerView()

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
            when (it.itemId) {
                R.id.feed -> {
                    val intent2 = Intent(this, FeedActivity::class.java)
                    startActivity(intent2)
                }
                R.id.following -> dLayout.closeDrawer(navigationView)
                R.id.myProfile -> {
                    val intent2 = Intent(this, BioActivity::class.java)
                    startActivity(intent2)
                }
            }

            true
        }
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
            this,"http://10.0.2.2:8080/api/v1/user",
            1,
            1,
            "111"
        )
        Log.d("FRIENDS IDS", result.toString())
        if (result.length > 2) { // TODO: try???
            val friendIdsList =
                jsonFormat.decodeFromString<List<Long>>(result)
            return friendIdsList
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetUserFriends(friendIdsList: List<Long>): List<User> {
        val result = Requests.GetUserFriends(
            this, "http://10.0.2.2:8080/api/v1/user",
            1,
            "111",
            friendIdsList)
        Log.d("FRIENDS", result.toString())
        val friendsList = Converters.stringToUsers(result.toString())
        return friendsList
    }


    override fun onItemLongClick(item: User, position: Int) {
        Log.d("AAA", "REGISTERED LONG CLICK")
    }

    override fun onItemClick(item: User, position: Int) {
        Log.d("AAA", "REGISTERED SHORT CLICK")
        val intent2 = Intent(this, FriendBioActivity::class.java)
        Log.d("ITEMID", item.id.toString())
        intent2.putExtra("id", item.id.toString())
        startActivity(intent2)
    }
}
