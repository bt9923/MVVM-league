package com.example.spanishleague.data.api

import com.example.spanishleague.BuildConfig.BASE_URL
import com.example.spanishleague.data.model.Team
import com.example.spanishleague.data.model.Teams
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/v1/json/1/lookup_all_teams.php?")
    suspend fun getAllTeams(@Query("id") idTeam: String) : Response<Teams>

    //https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=134221
    @GET("/api/v1/json/1/lookupteam.php?id=134221")
    suspend fun getTeamDetail(@Query("id") idTeam : String) : Response<Teams>
//    suspend fun getTeamDetail() : Response<Teams>
}

object RetrofitService {

    var client : OkHttpClient = OkHttpClient().newBuilder().build()

    private var retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).client(client).build()

    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}