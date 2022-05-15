package com.example.Auth

import android.os.AsyncTask
import android.util.Log
import okhttp3.*

class Requests {
//    internal class getUserProfileInfo(/*val url : String, val id : Int, val token : String*/) :
//        AsyncTask<Void, Void, String>() {
//
//        override fun doInBackground(vararg params: Void?): String {
//            val client = OkHttpClient()
//            val url = "http://10.0.2.2:8080/api/v1/profile/1/1"
//            val request = Request.Builder().url(url).addHeader("token", "111").build()
//            val response = client.newCall(request).execute()
//            return response.body()?.string().toString()
//        }
//    }

    internal class PostButtonRequest(/*val url : String, val id : Int, val token : String,*/
        val requestBodyMap: Map<String, String>, val knopka: KnopkaFeedAdapter, val ind: Int
    ) :
        AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val url = "http://10.0.2.2:8080/api/v1/knopka"
            val knopkaDTO: RequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                requestBodyMap.toString().replace("=", ":")
            )

            Log.d("KNOPKADTO", knopkaDTO.toString())

            val httpBuilder = HttpUrl.parse(url)!!.newBuilder()
            httpBuilder.addQueryParameter("knopkaUserId", "1")

            val request = Request.Builder()
                .url(httpBuilder.build())
                .addHeader("token", "111")
                .post(knopkaDTO)
                .build()
            val response = client.newCall(request).execute()
            return response.body()?.string().toString()
        }

    }


    internal class PutChangeInfoRequest(/*val url : String, val id : Int, val token : String, */
        val requestBodyMap: Map<String, String>
    ) :
        AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            Log.d("BBBB", requestBodyMap.toString())
            val client = OkHttpClient()
            val url = "http://10.0.2.2:8080/api/v1/profile/1"
            val profileDTO: RequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                requestBodyMap.toString().replace("=", ":")
            )

            Log.d("AAAA", profileDTO.toString())

            val request = Request.Builder().url(url)
                .addHeader("token", "111")
                .put(profileDTO)
                .build()
            val response = client.newCall(request).execute()
            return response.body()?.string().toString()
        }
    }

    internal class GetUserKnopkaIds(/*val url : String, val id : Int, val token : String*/) :
        AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val url = "http://10.0.2.2:8080/api/v1/user/1/1/knopkasId"
            val request = Request.Builder().url(url).addHeader("token", "111").build()
            val response = client.newCall(request).execute()
            return response.body()?.string().toString()
        }
    }
}

internal class GetUserKnopkas(/*val url : String, val id : Int, val token : String*/
    val knopkasIdList: List<Long>
) :
    AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:8080/api/v1/knopka/1/getbyids"
        val httpBuilder = HttpUrl.parse(url)!!.newBuilder()
        for (id in knopkasIdList) {
            httpBuilder.addQueryParameter("ids", id.toString())
        }
        val request =
            Request.Builder().url(httpBuilder.build()).addHeader("token", "111").build()
        val response = client.newCall(request).execute()
        return response.body()?.string().toString()
    }
}