package com.example.Auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.Auth.Regions.regions
import com.example.Auth.Requests.GetAllExistingRegions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.ArrayList

private val jsonFormat = Json {
    coerceInputValues = true; ignoreUnknownKeys = true
}

object Regions {
    var regions: MutableList<String> = ArrayList()
}

class CountryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val res = GetAllExistingRegions(
            "http://10.0.2.2:8080/api/v1/click/validregions",
            "111"
        )
        regions.addAll(jsonFormat.decodeFromString<List<String>>(res))
    }

}