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
        context: Context,
        url: String, id: Long,
        token: String, requestId: Long
    ): String =
        SendGetRequest<Nothing>(context, "$url/$requestId", token, null, null).execute().get()


    fun GetUserKnopkaIds(
        context: Context,
        url: String, idOwner: Long,
        idSender: Long, token: String
    ): String =
        SendGetRequest<Void>(
            context,
            "$url/$idOwner/knopkasId",
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
        SendGetRequest<Long>(context, "$url/knopka/getbyids", token, knopkasIdList, "ids").execute()
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
            "$url/$idOwner/friendsId",
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
            "$url/friends",
            token,
            friendsIdList,
            "friendsId"
        ).execute().get()

    fun GetAllKnopkas(
        context: Context,
        url: String, idSender: Long, token: String
    ): String =
        SendGetRequest<Void>(context, "$url/getall", token, null, null).execute().get()

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

    fun GetAllExistingRegions(
        url: String, token: String
    ): String = SendGetRequest<Void>(null, url, token, null, null).execute().get()

    fun GetKnopkasByRegion(url: String, region: String, token: String): String {
        return SendGetRequest<Long>(null, "$url/top/$region", token, null, null).execute().get()
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
            .get()?.body()
            ?.string().toString()
    }


    fun PostDescriptionRequest(
        context: Context,
        url: String, idButton: Long, token: String,
        requestBodyMap: Map<String, String>
    ): String {
        val descriptionDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            context,
            "$url/$idButton",
            token,
            descriptionDTO,
            idButton.toString(),
            ""
        ).execute()
            .get()?.body()
            ?.string().toString()
    }

    fun PostBatchRequest(
        url: String, id: Int, token: String,
        requestBodyMap: Map<String, Any?>
    ): Response {
        val batchDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            requestBodyMap.toString().replace("=", ":")
        )
        return SendPostRequest(
            null,
            url,
            token,
            batchDTO,
            null,
            null
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
            ?.body()
            ?.string().toString()
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
            ?.body()
            ?.string().toString()
    }

    fun PutAddFriendRequest(
        context: Context,
        url: String, id: Int, token: String,
        friendId: Long
    ): String {
        val requestDTO: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Json.Default.toString().replace("=", ":")
        )
        Log.d("URL: ", "$url/$id")
        return SendPutRequest(
            context,
            "$url/$id",
            token, requestDTO,
            friendId.toString(),
            "friendId"
        ).execute().get()
            ?.body()
            ?.string().toString()
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

//            if (parameters != null && parameterName != null) {
//                for (param in parameters) {
//                    Log.d("pARARM", param.toString())
//                    httpBuilder.addQueryParameter(parameterName, param.toString())
//                }
//            }
//            if (parameterName2 != null) {
//                httpBuilder.addQueryParameter(
//                    parameterName2,
//                    parameters?.get(parameters.size - 1)?.toString()
//                )
//
//            }

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
        AsyncTask<Void, Void, Response>() {
//        private val pdia = ProgressDialog(context);

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

        override fun onPreExecute() {
            super.onPreExecute()
//            pdia.setMessage("Loading...")
//            pdia.show()
        }

        override fun onPostExecute(result: Response) {
//            pdia.dismiss()
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
        AsyncTask<Void, Void, Response?>() {
        private val pdia = ProgressDialog(context);

        //
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
        Log.d("RESPONSE STATUS CODE", code.toString())

        when (code) {
            400 -> {

            }
            200 -> {

            }
        }
    }


}
