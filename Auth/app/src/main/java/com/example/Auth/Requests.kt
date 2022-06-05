package com.example.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.SearchView
import kotlinx.serialization.json.Json
import okhttp3.*

object Requests {
    // gets
    fun GetUserProfileInfo(
        context: Context,
        url: String, id: Long,
        token: String, requestId: Long
    ): String =
        SendGetRequest<Nothing>(context, "$url/$requestId/$id", token, null, null).execute().get()


    fun GetUserKnopkaIds(
        context: Context,
        url: String, idOwner: Long,
        idSender: Long, token: String
    ): String =
        SendGetRequest<Void>(
            context,
            "$url/$idSender/$idOwner/knopkasId",
            token,
            null,
            null
        ).execute().get()

    fun GetUserKnopkas(
        context: Context?,
        url: String, id: Int, token: String,
        knopkasIdList: List<Long>
    ):
            String =
        SendGetRequest<Long>(context, "$url/$id/getbyids", token, knopkasIdList, "ids").execute()
            .get()

    fun GetUserFriendsIds(
        context: Context,
        url: String,
        idSender: Int,
        idOwner: Int,
        token: String
    ):
            String =
        SendGetRequest<Nothing>(
            context,
            "$url/$idSender/$idOwner/friendsId",
            token,
            null,
            null
        ).execute()
            .get()

    fun GetUserFriends(
        context: Context,
        url: String, id: Int, token: String,
        friendsIdList: List<Long>
    ):
            String =
        SendGetRequest<Long>(
            context,
            "$url/$id/friends",
            token,
            friendsIdList,
            "friendsId"
        ).execute().get()

    fun GetAllKnopkas(
        context: Context,
        url: String, idSender: Long, token: String
    ): String =
        SendGetRequest<Void>(context, "$url/$idSender/getall", token, null, null).execute().get()

    fun GetKnopkaDescription(
        context: Context,
        url: String,
        token: String, requestId: Long, knopkaUserId: Long, paramName: String,
    ): String {
        val al = ArrayList<Long>()
        al.add(knopkaUserId)
        Log.d("AAAAAAAAAAAAAAAA", "$url/description/$requestId")
        return SendGetRequest<Long>(
            context,
            "$url/description/$requestId",
            token,
            al,
            paramName
        ).execute()
            .get()
    }

    fun GetKnopkasByTag(
        url: String,
        token: String,
        tag: String,
        knopkaUserId: Long,
        parameterName: String?,
        parameterName2: String?
    ): String {
        val al = ArrayList<Any>()
        al.add(tag)
        al.add(knopkaUserId)
        Log.d("tagReq", tag)
        return SendGetRequest(
            null,
            "$url/description/tag",
            token,
            al,
            parameterName,
            parameterName2
        ).execute().get()

    }

    // posts
    fun PostKnopkaRequest(
        context: Context,
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, String>
    ): String {
        val knopkaDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            context,
            url,
            token,
            knopkaDTO,
            id.toString(),
            "knopkaUserId"
        ).execute()
            .get()
    }

    fun PostDescriptionRequest(context: Context,
                               url: String, idButton: Long, token: String,
                               requestBodyMap: Map<String, String>
    ): String {
        val descriptionDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(context, "$url/$idButton", token, descriptionDTO, idButton.toString(), "").execute()
            .get()
    }

    fun PostBatchRequest(
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, Any?>
    ): String {
        val batchDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            null,
            "$url/$id",
            token,
            batchDTO,
           null,
            null
        ).execute()
            .get()
    }

    fun TESTcLICK(
        context: Context
    ): String {
        val knopkaDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            "requestBodyMap".toString().replace("=", ":")
        )
        return SendPostRequest(
            context,
            "http://10.0.2.2:8080/api/v1/click",
            "111",
            knopkaDTO,
            "1",
            "knopkaId"
        ).execute()
            .get()
    }

    // puts
    fun PutDescriptionRequest(
        context: Context,
        url: String, idButton: Long, token: String,
        requestBodyMap: Map<String, Any?>
    ): String {
        Log.d("req", requestBodyMap.toString())
        val descriptionDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPutRequest(
            context,
            "$url/$idButton",
            token,
            descriptionDTO,
            idButton.toString(),
            ""
        ).execute()
            .get()
    }
    
    fun PutChangeInfoRequest(
        context: Context,
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, String>
    ): String {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPutRequest(context, "$url/$id", token, requestDTO, null, null).execute().get()
    }

    fun PutAddFriendRequest(
        context: Context,
        url: String, id: Int, token: String,
        friendId: Long
    ): AsyncTask<Void, Void, String>? {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Json.Default.toString().replace("=", ":")
        )
        return SendPutRequest(
            context,
            "$url/$id",
            token, requestDTO,
            friendId.toString(),
            "friendId"
        ).execute()
    }

    internal class SendGetRequest<T>(
        private val context: Context?,
        private val url: String,
        private val token: String,
        private val parameters: List<T>?,
        private val parameterName: String?,
        private val parameterName2: String? = null

    ) :
        AsyncTask<Void, Void, String>() {
        //        private val pdia = ProgressDialog(context);
        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()

            if (parameterName2 != null) {
                httpBuilder.addQueryParameter(parameterName, parameters?.get(0)?.toString())
                httpBuilder.addQueryParameter(parameterName2, parameters?.get(1)?.toString())
            } else {
                if (parameters != null && parameterName != null) {
                    for (param in parameters) {
                        Log.d("pARARM", param.toString())
                        httpBuilder.addQueryParameter(parameterName, param.toString())
                    }
                }
            }

            val request =
                Request.Builder().url(httpBuilder.build()).addHeader("token", token)
                    .build()

            val response = client.newCall(request).execute()

            checkStatusCode(response.code())

            return response.body()?.string().toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
//            pdia.setMessage("Loading...")
//            pdia.show()
        }

        override fun onPostExecute(result: String?) {
//            pdia.dismiss()
        }

    }

    internal class SendPostRequest(
        private val context: Context?,
        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, String>() {
        private val pdia = ProgressDialog(context);

        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()

            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()

            if (parameterName != null) {
                httpBuilder.addQueryParameter(parameterName, parameter)
            }

            val request = Request.Builder()
                .url(httpBuilder.build())
                .addHeader("token", token)
                .post(requestDTO)
                .build()

            val response = client.newCall(request).execute()

            checkStatusCode(response.code())

            return response.body()?.string().toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pdia.setMessage("Loading...")
            pdia.show()
        }

        override fun onPostExecute(result: String?) {
            pdia.dismiss()
        }
    }


    internal class SendPutRequest(
        private val context: Context,

        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody?,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, String>() {
        private val pdia = ProgressDialog(context);

        override fun doInBackground(vararg params: Void?): String {

            val client = OkHttpClient()

            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()

            if (parameterName != null) {
                httpBuilder.addQueryParameter(parameterName, parameter)
            }

            val requestPart = Request.Builder()
                .url(httpBuilder.build())
                .addHeader("token", token)

            val request: Request = if (requestDTO != null) {
                requestPart.put(requestDTO).build()
            } else {
                requestPart.put(requestDTO).build()
            }

            val response = client.newCall(request).execute()

            checkStatusCode(response.code())

            return response.body()?.string().toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pdia.setMessage("Loading...")
            pdia.show()
        }

        override fun onPostExecute(result: String?) {
            pdia.dismiss()
        }
    }


    fun checkStatusCode(code: Int) {
        Log.d("RESPONSE STATUS CODE", code.toString())

        when (code) {
            400 -> {

            }
            200 -> {

            }
        }
    }


}

