package com.example.Auth

import android.content.Intent
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity

class ToolbarItemSetter(
    var callingFrom: String,
    var item: MenuItem,
    var context: AppCompatActivity
) {
    fun set() {
        if (callingFrom == "Bio") run {
            when (item.itemId) {
                R.id.logOutIcon -> {
                    val intentLogOut = Intent(context, ProfileActivity::class.java)
                    startActivity(context, intentLogOut, null)
                }
                R.id.addKnopkaIcon -> {
                    val intentCreateKnopka = Intent(context, CreateButtonActivity::class.java)
                    startActivityForResult(
                        context,
                        intentCreateKnopka,
                        RequestCodes.CREATE_BUTTON_REQUEST_CODE,
                        null
                    )
                }
            }
        }
//        if (callingFrom == "Feed" || callingFrom == "Foollowing") run {
//            when (item.itemId) {
//                R.id.searchKnopkaIcon -> {
////                    val searchView = R.id.searchKnopkaIcon?.actionView as SearchView
//
//                }
//            }
//        }
    }
}