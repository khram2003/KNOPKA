package com.example.Auth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

object ClickBatch {
    var clicks: MutableMap<Long, Long> = HashMap()
    var startTime: LocalDateTime = LocalDateTimeEx.getNow(0)
    var working: Boolean = false
    fun setClicks(id: Long) {
        if (!clicks.containsKey(id)) {
            clicks[id] = 0
        }
        clicks[id] = clicks[id]?.plus(1) as Long

    }
}

public class BatchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        val b = Batch(ClickBatch.clicks, ClickBatch.startTime)
        for (i in ClickBatch.clicks){
            val bodyMap = mapOf<String, Any?>(
                "\"time\"" to '"' + ClickBatch.startTime.toString() + '"',
                "\"pushes\"" to i.value,
                "\"region\"" to '"' +"ggg" + '"',
                "\"clickedKnopkaId\"" to i.key
            )
            Log.d("ddddd", bodyMap.toString())
            val res = Requests.PostBatchRequest(
                "http://10.0.2.2:8080/api/v1/click/batch",
                1,
                "111",
                bodyMap
            )
            Log.d("RESS", res)
        }


        Log.d("CLICKS", ClickBatch.clicks.toString())
        Log.d("ASASASASS", "WORKING))))")

        ClickBatch.working = false
        ClickBatch.startTime = LocalDateTimeEx.getNow(0)
        ClickBatch.clicks = HashMap()
    }

}


