package com.example.spanishleague.repository

import android.util.Log
import com.example.spanishleague.data.api.ApiService
import com.example.spanishleague.data.model.Team
import com.example.spanishleague.data.model.Teams
import retrofit2.Response
import java.lang.Exception

class Repository(val apiService: ApiService) {

    private val TAG = Repository::class.java.name

    suspend fun getAllTeams(idTeam: String): Response<Teams>? {
        try {
            val response = apiService.getAllTeams(idTeam)
            response.let {
                return  it
            }
        } catch (error : Exception){
            Log.e(TAG, "ERROR: ${error.message}")
            return null
        }
        return null
    }

    suspend fun getTeamDetail(idTeam : String) : Response<Teams>? {
        try {
            val response = apiService.getTeamDetail(idTeam)
//            val response = apiService.getTeamDetail()
            response.let {
                return it
            }
        } catch (error : Exception){
            Log.e(TAG, "ERROR: ${error.message}")
            return null
        }
        return null
    }

}