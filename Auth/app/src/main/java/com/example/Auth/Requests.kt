package com.example.Auth

import android.os.AsyncTask
import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.*

object Requests {
    // gets
    fun GetUserProfileInfo(
        url: String, id: Long,
        token: String, requestId: Long
    ): String = SendGetRequest<Nothing>("$url/$requestId/$id", token, null, null).execute().get()

    fun GetUserKnopkaIds(
        url: String, idSender: Int,
        idOwner: Int, token: String
    ): String =
        SendGetRequest<Void>("$url/$idSender/$idOwner/knopkasId", token, null, null).execute().get()

    fun GetUserKnopkas(
        url: String, id: Int, token: String,
        knopkasIdList: List<Long>
    ):
            String =
        SendGetRequest<Long>("$url/$id/getbyids", token, knopkasIdList, "ids").execute().get()

    fun GetUserFriendsIds(
        url: String,
        idSender: Int,
        idOwner: Int,
        token: String
    ):
            String =
        SendGetRequest<Nothing>("$url/$idSender/$idOwner/friendsId", token, null, null).execute()
            .get()

    fun GetUserFriends(
        url: String, id: Int, token: String,
        friendsIdList: List<Long>
    ):
            String =
        SendGetRequest<Long>("$url/$id/friends", token, friendsIdList, "friendsId").execute().get()

    // posts

    fun PostKnopkaRequest(
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, String>
    ): String {
        val knopkaDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(url, token, knopkaDTO, id.toString(), "knopkaUserId").execute()
            .get()
    }

    // puts
    fun PutChangeInfoRequest(
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, String>
    ): String {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPutRequest("$url/$id", token, requestDTO, null, null).execute().get()
    }


    internal class SendGetRequest<T>(
        private val url: String,
        private val token: String,
        private val parameters: List<T>?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()

            if (parameters != null && parameterName != null) {
                for (param in parameters) {
                    httpBuilder.addQueryParameter(parameterName, param.toString())
                }
            }

            val request =
                Request.Builder().url(httpBuilder.build()).addHeader("token", token)
                    .build()

            val response = client.newCall(request).execute()

            if (response.code() == 200) {
                //TODO: deal with return status
            }

            return response.body()?.string().toString()
        }
    }

    internal class SendPostRequest(
        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, String>() {

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

    }


    fun PutAddFriendRequest(
        url: String, id: Int, token: String,
        friendId: Long
    ): AsyncTask<Void, Void, String>? {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Json.Default.toString().replace("=", ":")
        )
        return SendPutRequest(
            "$url/$id",
            token, requestDTO,
            friendId.toString(),
            "friendId"
        ).execute()
    }

    internal class SendPutRequest(
        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody?,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, String>() {
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

