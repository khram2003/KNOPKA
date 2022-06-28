package com.example.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.*
import java.util.concurrent.TimeUnit

object Requests {
    // gets
    fun GetUserProfileInfo(
        requestId: Long
    ): String =
        SendGetRequest<Nothing>(
            ThisDb.dbInfo.url + "api/v1/profile/$requestId",
            ThisUser.userInfo.token,
            null,
            null
        ).execute().get()


    fun GetUserKnopkaIds(
        context: Context, idOwner: Long,
    ): String =
        SendGetRequest<Void>(
            ThisDb.dbInfo.url + "api/v1/user/$idOwner/knopkasId",
            ThisUser.userInfo.token,
            null,
            null
        ).execute().get()

    fun GetUserKnopkas(
        knopkasIdList: List<Long>
    ):
            String =
        SendGetRequest(
            ThisDb.dbInfo.url + "api/v1/knopka/getbyids",
            ThisUser.userInfo.token,
            knopkasIdList,
            "ids"
        ).execute()
            .get()

    fun GetUserFriendsIds(
        idOwner: Long,
    ):
            String =
        SendGetRequest<Nothing>(
            ThisDb.dbInfo.url + "api/v1/user/$idOwner/friendsId",
            ThisUser.userInfo.token,
            null,
            null
        ).execute()
            .get()

    fun GetUserFriends(
        friendsIdList: List<Long>
    ):
            String =
        SendGetRequest(
            ThisDb.dbInfo.url + "api/v1/user/friends",
            ThisUser.userInfo.token,
            friendsIdList,
            "friendsId"
        ).execute().get()

    fun GetAllKnopkas(
    ): String =
        SendGetRequest<Void>(
            ThisDb.dbInfo.url + "api/v1/knopka/getall",
            ThisUser.userInfo.token,
            null,
            null
        ).execute().get()

    fun GetKnopkaDescription(
        requestId: Long, knopkaUserId: Long, paramName: String,
    ): String {
        val al = ArrayList<Long>()
        al.add(knopkaUserId)

        return SendGetRequest(
            ThisDb.dbInfo.url + "api/v1/description/$requestId",
            ThisUser.userInfo.token,
            al,
            paramName
        ).execute()
            .get()
    }

    fun GetKnopkasByTag(
        tag: String,
        knopkaUserId: Long,
        parameterName: String?,
        parameterName2: String?
    ): String {
        val al = ArrayList<Any>()
        al.add(tag)
        al.add(knopkaUserId)

        return SendGetRequest(
            ThisDb.dbInfo.url + "api/v1/description/tag",
            ThisUser.userInfo.token,
            al,
            parameterName,
            parameterName2
        ).execute().get()

    }

    fun GetAllExistingRegions(
    ): String = SendGetRequest<Void>(
        ThisDb.dbInfo.url + "api/v1/click/validregions",
        ThisUser.userInfo.token,
        null,
        null
    ).execute().get()

    fun GetKnopkasByRegion(region: String): String {
        return SendGetRequest<Long>(
            ThisDb.dbInfo.url + "api/v1/click/top/$region",
            ThisUser.userInfo.token,
            null,
            null
        ).execute()
            .get()
    }

    // posts
    fun PostKnopkaRequest(
        id: Long,
        requestBodyMap: Map<String, String>
    ): String {
        val knopkaDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            ThisDb.dbInfo.url + "api/v1",
            ThisUser.userInfo.token,
            knopkaDTO,
            id.toString(),
            "knopkaUserId"
        ).execute()
            .get()?.body()
            ?.string().toString()
    }


    fun PostBatchRequest(
        requestBodyMap: Map<String, Any?>
    ): Response {
        val batchDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            ThisDb.dbInfo.url + "api/v1/click/batch",
            ThisUser.userInfo.token,
            batchDTO,
            null,
            null
        ).execute()
            .get()
    }

    // puts
    fun PutDescriptionRequest(
        context: Context,
        idButton: Long,
        requestBodyMap: Map<String, Any?>
    ): String {

        val descriptionDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPutRequest(
            context,
            ThisDb.dbInfo.url + "api/v1/description/$idButton",
            ThisUser.userInfo.token,
            descriptionDTO,
            idButton.toString(),
            ""
        ).execute()
            .get()
            ?.body()
            ?.string().toString()
    }

    fun PutChangeInfoRequest(
        context: Context,
        id: Long,
        requestBodyMap: Map<String, String>
    ): String {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPutRequest(
            context,
            ThisDb.dbInfo.url + "api/v1/profile/$id",
            ThisUser.userInfo.token,
            requestDTO,
            null,
            null
        ).execute().get()
            ?.body()
            ?.string().toString()
    }

    fun PutAddFriendRequest(
        context: Context,
        friendId: Long
    ): String {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Json.Default.toString().replace("=", ":")
        )
        return SendPutRequest(
            context,
            ThisDb.dbInfo.url + "api/v1/user/$friendId",
            ThisUser.userInfo.token, requestDTO,
            friendId.toString(),
            "friendId"
        ).execute().get()
            ?.body()
            ?.string().toString()
    }

    internal class SendGetRequest<T>(
        private val url: String,
        private val token: String,
        private val parameters: List<T>?,
        private val parameterName: String?,
        private val parameterName2: String? = null

    ) :
        AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()

            if (parameterName2 != null) {
                httpBuilder.addQueryParameter(parameterName, parameters?.get(0)?.toString())
                httpBuilder.addQueryParameter(parameterName2, parameters?.get(1)?.toString())
            } else {
                if (parameters != null && parameterName != null) {
                    for (param in parameters) {

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

    }

    internal class SendPostRequest(
        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, Response>() {

        override fun doInBackground(vararg params: Void?): Response? {
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

            return response
        }

    }


    internal class SendPutRequest(
        context: Context,
        private val url: String,
        private val token: String,
        private val requestDTO: RequestBody?,
        private val parameter: String?,
        private val parameterName: String?
    ) :
        AsyncTask<Void, Void, Response?>() {
        private val pdia = ProgressDialog(context);


        override fun doInBackground(vararg params: Void?): Response? {

            val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();

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

            return response
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pdia.setMessage("Loading...")
            pdia.show()
        }

        override fun onPostExecute(result: Response?) {
            pdia.dismiss()
        }
    }


    fun checkStatusCode(code: Int) {
        when (code) {
            in 400..505 -> {
                TODO()
            }
        }
    }


}
