package com.example.spanishleague.ui.Teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spanishleague.data.api.ApiService
import com.example.spanishleague.repository.Repository

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsViewModel::class.java)){
            return TeamsViewModel(Repository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}