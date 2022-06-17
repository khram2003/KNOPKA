package com.example.Auth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
//import androidx.work.Worker
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDateTime
import java.util.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.collections.HashMap

class ClickBatch {
    var clicks: MutableMap<Long, Long> = HashMap()
    var startTime: LocalDateTime = LocalDateTimeEx.getNow(0)
}

object BatchesToAdd {
    var clicks: MutableList<Batch> = ArrayList()
}

object CurBatch {
    var clickBatch: ClickBatch = ClickBatch()

    var working: Boolean = false
    fun setClicks(id: Long) {
        if (!clickBatch.clicks.containsKey(id)) {
            clickBatch.clicks[id] = 0
        }
        clickBatch.clicks[id] = clickBatch.clicks[id]?.plus(1) as Long
    }
}


class BatchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        for (i in CurBatch.clickBatch.clicks) {
            val bodyMap = mapOf<String, Any?>(
                "\"time\"" to '"' + CurBatch.clickBatch.startTime.toString() + '"',
                "\"pushes\"" to i.value,
                "\"region\"" to '"' + ThisUser.userInfo.location + '"',
                "\"clickedKnopkaId\"" to i.key,
                "\"authorId\"" to 1
            )
            Log.d("ddddd", bodyMap.toString())
            val res = Requests.PostBatchRequest(
                "http://10.0.2.2:8080/api/v1/click/batch",
                1,
                "111",
                bodyMap
            )
            Log.d("RESS", res.toString())
            if (res.code() != 200 || res.toString() == ""
            ) {
                BatchesToAdd.clicks.plus(
                    Batch(
                        i.key,
                        i.value,
                        CurBatch.clickBatch.startTime.toString()
                    )
                )
            }
        }

        for (i in BatchesToAdd.clicks) {
            val bodyMap = mapOf<String, Any?>(
                "\"time\"" to '"' + CurBatch.clickBatch.startTime.toString() + '"',
                "\"pushes\"" to i.clicks,
                "\"region\"" to '"' + "ggg" + '"',
                "\"clickedKnopkaId\"" to i.id
            )
            Log.d("ddddd", bodyMap.toString())
            val res = Requests.PostBatchRequest(
                "http://10.0.2.2:8080/api/v1/click/batch",
                1,
                "111",
                bodyMap
            )
            Log.d("RESS", res.toString())
            if (res.code() == 200 /*&& res.body()
                    ?.string().toString() == ""//TODO what*/
            ) {
                BatchesToAdd.clicks.remove(i)
            }
        }


        Log.d("CLICKS", CurBatch.clickBatch.clicks.toString())
        Log.d("ASASASASS", "WORKING))))")

        CurBatch.working = false
        CurBatch.clickBatch.startTime = LocalDateTimeEx.getNow(0)
        CurBatch.clickBatch.clicks = HashMap()


    }

}

//
//class MyWorker(context: Context/*, params: WorkerParameters*/) : Worker() {
//    override fun doWork(): WorkerResult {
//        try {
//            Log.d("IN WORKER", "DOIN WORK")
//        } catch (ex: Exception) {
//            return WorkerResult.FAILURE//или Result.Retry
//        }
//        return WorkerResult.SUCCESS
//    }
//
//}