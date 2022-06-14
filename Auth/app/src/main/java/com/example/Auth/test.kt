//
//package com.example.Auth
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.AsyncTask
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import org.threeten.bp.LocalDateTime
//import java.util.*
//import kotlin.collections.HashMap
//
//private val jsonFormat = Json {
//    coerceInputValues = true; ignoreUnknownKeys = true
//}
//
//class ClickBatchHandler : AppCompatActivity() {
//
//    fun addBatchToStorage(batch : Batch){
//
//        val sharedPref = getSharedPreferences("mypref1", 0)
//        val editor = sharedPref.edit()
//
//        val batches: List<Batch> =
//            jsonFormat.decodeFromString(sharedPref.getString("batches", "").toString())
//        batches.plus(batch);
//        editor.putString("batches", jsonFormat.encodeToString(batches))
//
//        editor.apply()
//
//    }
//    object ClickBatch {
//        var clicks: MutableMap<Long, Long> = HashMap()
//        var startTime: LocalDateTime = LocalDateTimeEx.getNow(0)
//        var working: Boolean = false
//        fun setClicks(id: Long) {
//            if (!clicks.containsKey(id)) {
//                clicks[id] = 0
//            }
//            clicks[id] = clicks[id]?.plus(1) as Long
//        }
//    }
//
//    public class BatchReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            for (i in ClickBatch.clicks){
//                val bodyMap = mapOf<String, Any?>(
//                    "\"time\"" to '"' + ClickBatch.startTime.toString() + '"',
//                    "\"pushes\"" to i.value,
//                    "\"region\"" to '"' +"ggg" + '"',
//                    "\"clickedKnopkaId\"" to i.key
//                )
//                Log.d("ddddd", bodyMap.toString())
//                val res = Requests.PostBatchRequest(
//                    "http://10.0.2.2:8080/api/v1/click/batch",
//                    1,
//                    "111",
//                    bodyMap
//                )
//                Log.d("RESS", res)
//                if(res.equals("")){
//                    ClickBatchHandler.addBatchToStorage(Batch())
//                }
//            }
//
//
//            Log.d("CLICKS", ClickBatch.clicks.toString())
//            Log.d("ASASASASS", "WORKING))))")
//
//            ClickBatch.working = false
//            ClickBatch.startTime = LocalDateTimeEx.getNow(0)
//            ClickBatch.clicks = HashMap()
//        }
//
//    }
//
//
//
//
//}