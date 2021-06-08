package com.example.spanishleague.ui.Teams

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spanishleague.R
import com.example.spanishleague.data.api.ApiService
import com.example.spanishleague.data.api.RetrofitService
import com.example.spanishleague.data.model.Team
import com.example.spanishleague.utils.Result
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //<editor-fold desc="Vars">

    private var teamsViewModel : TeamsViewModel? = null

    //<editor-folder>

    //<editor-fold desc="Life cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBindings()
        initViewModels()
        initObservers()

    }

    override fun onDestroy() {
        teamsViewModel?.listState = recyclerTeams.layoutManager?.onSaveInstanceState()
        super.onDestroy()
    }

    //<editor-folder>

    //<editor-fold desc="Init">

    private fun initBindings() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerTeams.run {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    private fun initViewModels() {
        if (teamsViewModel == null) {
            teamsViewModel = ViewModelProvider(this@MainActivity,
                ViewModelFactory(RetrofitService.createService(ApiService::class.java))).get(
                TeamsViewModel::class.java)
            teamsViewModel?.loadData()
        }
    }

    private fun initObservers() {
        teamsViewModel?.getTeams()?.observe(this, Observer { result ->
            when(result) {
                is Result.Success -> {
                    renderList(result.data)
//                    EspressoIdlingResource.decrement()
//                    homePhotosProgressContainer.visibility = View.GONE
                    recyclerTeams.visibility = View.VISIBLE
                }
                is Result.InProgress -> {
//                    homePhotosProgressContainer.visibility = View.VISIBLE
                    recyclerTeams.visibility = View.GONE
                }
                is Result.Error -> {
//                    homePhotosProgressContainer.visibility = View.GONE
                    Toast.makeText(this, result.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(teams: MutableList<Team>) {
        if (teams.isNotEmpty()) {
            //when screen starts
            if (teamsViewModel?.getCurrentPage() == 1 || recyclerTeams.adapter?.itemCount == 0) {
                setRecyclerData(teams)
            } else { //when load more
                if (recyclerTeams.adapter == null) { //after load more
                    setRecyclerData(teams)
                }
                recyclerTeams.adapter?.notifyDataSetChanged()
            }
            //load state of rv
            if (teamsViewModel?.listState != null) {
                recyclerTeams.layoutManager?.onRestoreInstanceState(teamsViewModel?.listState)
                teamsViewModel?.listState = null
            }
        } else {
            showSnackBarMessage()
        }
    }

    //<editor-folder>

    private fun setRecyclerData(teams: MutableList<Team>) {
        with(recyclerTeams) {
            adapter = TeamsAdapter(teams, this@MainActivity)
        }
    }

    private fun showSnackBarMessage() {
//        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav)!!
        Toast.makeText(this@MainActivity, R.string.no_data_msg, Toast.LENGTH_SHORT).show()
    }
}