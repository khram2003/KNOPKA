package com.example.Auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.threeten.bp.LocalDateTime
import java.util.*
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

            val res = Requests.PostBatchRequest(
                bodyMap
            )

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

            val res = Requests.PostBatchRequest(
                bodyMap
            )

            if (res.code() == 200) {
                BatchesToAdd.clicks.remove(i)
            }
        }


        CurBatch.working = false
        CurBatch.clickBatch.startTime = LocalDateTimeEx.getNow(0)
        CurBatch.clickBatch.clicks = HashMap()


    }

}