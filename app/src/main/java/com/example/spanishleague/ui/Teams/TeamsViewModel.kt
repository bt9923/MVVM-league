package com.example.spanishleague.ui.Teams

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spanishleague.data.model.Team
import com.example.spanishleague.repository.Repository
import com.example.spanishleague.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TeamsViewModel(private val repository: Repository) : ViewModel() {

    private val teams = MutableLiveData<Result<MutableList<Team>>>()
    private val teamDetail = MutableLiveData<Result<MutableList<Team>>>()
    private var currentPage = 1
    var listState: Parcelable? = null //to save and restore rv's adapter

    fun getTeams() = teams

    fun getTeamDetail() = teamDetail

    fun getCurrentPage() = currentPage

    fun loadData(idTeam: String) {
        try {
            if (currentPage == 1){
                teams.postValue(Result.InProgress)
            }
            viewModelScope.launch(Dispatchers.IO){
                val response = repository.getAllTeams(idTeam)
                response?.let {
                    val teamList = it.body()?.teams
                    teamList?.let { list ->
                        if (currentPage == 1){
                            teams.postValue(Result.Success(list))
                        }else{
                            val currentTeam : MutableList<Team>? = teams.value?.extractData
                            if (currentTeam == null || currentTeam.isEmpty()){
                                teams.postValue(Result.Success(list))
                            }else{
                                currentTeam.addAll(list)

                                teams.postValue(Result.Success(currentTeam))
                            }
                        }
                    } ?: run {
                        teams.postValue(Result.Success(arrayListOf()))
                    }
                }?: run {
                    teams.postValue(Result.Success(arrayListOf()))
                }
            }
        } catch (error : Exception){
            teams.postValue(Result.Error(error))
        }
    }

    fun loadTeamDetail(idTeam : String){
        try {
            viewModelScope.launch(Dispatchers.IO){
                val response = repository.getTeamDetail(idTeam)
                response.let {
                    val teamDetailInner = it?.body()?.teams
                    teamDetailInner.let { result ->
                        teamDetail.postValue(Result.Success(result!!))
                    }
                }
            }
        }catch (error : Exception){
            teamDetail.postValue(Result.Error(error))
        }
    }
}