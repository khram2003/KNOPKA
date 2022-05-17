package com.example.Auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class FollowingActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_following)

        // toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.AllComponentsColor)))

        // slideout menu
        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayoutFollowing)
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navViewFollowing)
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
}
