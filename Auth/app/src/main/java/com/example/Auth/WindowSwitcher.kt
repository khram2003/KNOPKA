package com.example.Auth

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class WindowSwitcher(
    val item: MenuItem,
    val context: AppCompatActivity,
) {
    fun set() {
        when (item.itemId) {
            R.id.feed -> {
                val feedActivity = Intent(context, FeedActivity::class.java)
                startActivity(context, feedActivity, null)
                Log.d("WindowSwitcherSetter", "switched from My Profile to feed")
            }

            R.id.following -> {
                val followingActivity = Intent(context, FollowingActivity::class.java)
                startActivity(context, followingActivity, null)
                Log.d("WindowSwitcherSetter", "switched from My Profile to following")
            }

            R.id.myProfile -> {
                val myProfileActivity = Intent(context, BioActivity::class.java)
                startActivity(context, myProfileActivity, null)
                Log.d("WindowSwitcherSetter", "switched from My Profile to My Profile")
            }
            else -> Log.d("WindowSwitcherSetter", "didn't complete switch")
        }
    }
}