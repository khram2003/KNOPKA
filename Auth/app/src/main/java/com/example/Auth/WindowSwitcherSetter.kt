package com.example.Auth

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class WindowSwitcherSetter(
    val callingFrom: String,
    val item: MenuItem,
    val context: AppCompatActivity,
    val dLayout: DrawerLayout,
    val navigationView: NavigationView
) {
    fun set() {
        if (callingFrom == "Bio") run {
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
                    dLayout.closeDrawer(navigationView)
                    Log.d("WindowSwitcherSetter", "switched from My Profile to My Profile")
                }
                else -> Log.d("WindowSwitcherSetter", "didn't complete switch")
            }
        }
        if (callingFrom == "Feed") run {
            when (item.itemId) {
                R.id.feed -> {
                    dLayout.closeDrawer(navigationView)
                    Log.d("WindowSwitcherSetter", "switched from feed to feed")
                }
                R.id.following -> {
                    val followingActivity = Intent(context, FollowingActivity::class.java)
                    startActivity(context, followingActivity, null)
                    Log.d("WindowSwitcherSetter", "switched from feed to following")
                }
                R.id.myProfile -> {
                    val myProfileActivity = Intent(context, BioActivity::class.java)
                    startActivity(context, myProfileActivity, null)
                    Log.d("WindowSwitcherSetter", "switched from feed to My Profile")
                }
                else -> Log.d("WindowSwitcherSetter", "didn't complete switch")
            }
        }
        if (callingFrom == "Following") run {
            when (item.itemId) {
                R.id.feed -> {
                    val feedActivity = Intent(context, FeedActivity::class.java)
                    startActivity(context, feedActivity, null)
                    Log.d("WindowSwitcherSetter", "switched from following to feed")
                }
                R.id.following -> {
                    dLayout.closeDrawer(navigationView)
                    Log.d("WindowSwitcherSetter", "switched from following to following")
                }
                R.id.myProfile -> {
                    val myProfileActivity = Intent(context, BioActivity::class.java)
                    startActivity(context, myProfileActivity, null)
                    Log.d("WindowSwitcherSetter", "switched from following to My Profile")
                }
                else -> Log.d("WindowSwitcherSetter", "didn't complete switch")
            }
        }
    }
}