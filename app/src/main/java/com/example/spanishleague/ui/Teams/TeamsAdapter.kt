package com.example.spanishleague.ui.Teams

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spanishleague.R
import com.example.spanishleague.data.api.ApiService
import com.example.spanishleague.data.api.RetrofitService
import com.example.spanishleague.data.model.Team
import com.example.spanishleague.utils.Result


class TeamsAdapter(var teams: MutableList<Team>, val application: MainActivity) : RecyclerView.Adapter<TeamsAdapter.ViewHolder>(){

    private var teamsViewModel : TeamsViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_list, parent, false)

        if (teamsViewModel == null) {
            teamsViewModel = ViewModelProvider(
                application,
                ViewModelFactory(RetrofitService.createService(ApiService::class.java))
            ).get(
                TeamsViewModel::class.java
            )
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(teams[position], application, teamsViewModel)
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var badgeTeam = view.findViewById<ImageView>(R.id.badgeTeam)
        private var nameTeam = view.findViewById<TextView>(R.id.nameTeam)
        private var stadiumTeam = view.findViewById<TextView>(R.id.stadiumTeam)
        private var cardView = view.findViewById<CardView>(R.id.cardView)
        var progressBar : ProgressBar? = null

        fun bind(team: Team, context: MainActivity, teamsViewModel: TeamsViewModel?){
            progressBar = ProgressBar(context)

            Glide
                .with(context).load(team.strTeamBadge).into(badgeTeam)

            nameTeam.text = team.strTeam
            stadiumTeam.text = team.strStadium

            cardView.setOnClickListener {
                teamsViewModel!!.loadTeamDetail(team.idTeam)
                showDialogDetailTeam(context, teamsViewModel, team.idTeam)
            }
        }

        private fun showDialogDetailTeam(
            context: MainActivity,
            teamsViewModel: TeamsViewModel?,
            idTeam: String
        ) {
            //        mDialogInfoAppts.setTitle(R.string.type_service_consultation);
            var mDialogDetail = Dialog(context)
            mDialogDetail.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialogDetail.setContentView(R.layout.info_appointment_available)
            val nameTeam = mDialogDetail.findViewById<TextView?>(R.id.nameTeam)
            val stadiumTeam = mDialogDetail.findViewById<TextView?>(R.id.stadiumTeam)
            val foundationYearTeam = mDialogDetail.findViewById<TextView?>(R.id.foundationYearTeam)
            val badgeTeam = mDialogDetail.findViewById<ImageView?>(R.id.imgPhysicianItem)

            val teamEvents = mDialogDetail.findViewById<TextView?>(R.id.teamEvents)
//            val hourAppts = mDialogInfoAppts!!.findViewById(R.id.hourAppointment)
            val webSiteTeam = mDialogDetail.findViewById<TextView>(R.id.webSiteTeam)
            val instagramTeam = mDialogDetail.findViewById<ImageView>(R.id.instagramTeam)
            val facebookTeam = mDialogDetail.findViewById<ImageView>(R.id.facebookTeam)
            val youtubeTeam = mDialogDetail.findViewById<ImageView>(R.id.youtubeTeam)
            val twitterTeam = mDialogDetail.findViewById<ImageView>(R.id.twitterTeam)
            val jerseyTeam = mDialogDetail.findViewById<ImageView>(R.id.jerseyTeam)
            val mBtnCancel = mDialogDetail.findViewById<Button?>(R.id.btnCancel)
            val btnBookAppt = mDialogDetail.findViewById<Button?>(R.id.btnBookAppt)

            webSiteTeam?.movementMethod = LinkMovementMethod.getInstance()
//            teamsViewModel!!.loadTeamDetail(idTeam)
            teamsViewModel!!.getTeamDetail().observe(context, Observer { result ->
                when (result) {
                    is Result.Success -> {
                        Glide
                            .with(context).load(result.data[0].strTeamBadge).into(badgeTeam!!)

                        nameTeam?.text = result.extractData!![0].strTeam
                        stadiumTeam?.text = "Estadio: ${result.extractData!![0].strStadium}"
                        foundationYearTeam?.text =
                            "Año de fundación: ${result.extractData!![0].intFormedYear} "
                        if (result.extractData != null) {
                            teamEvents?.text =
                                "${if (result.extractData!![0].strLeague == null || result.extractData!![0].strLeague.isEmpty()) "" else "-${result.extractData!![0].strLeague}\n"}${if (result.extractData!![0].strLeague2 == null || result.extractData!![0].strLeague2.isEmpty()) "" else "-${result.extractData!![0].strLeague2}\n"}" +
                                        "${if (result.extractData!![0].strLeague3 == null || result.extractData!![0].strLeague3.isEmpty()) "" else "-${result.extractData!![0].strLeague3}\n"}${if (result.extractData!![0].strLeague4 == null || result.extractData!![0].strLeague4.isEmpty()) "" else "-${result.extractData!![0].strLeague4}\n"}" +
                                        "${if (result.extractData!![0].strLeague5 == null || result.extractData!![0].strLeague5.isEmpty()) "" else "-${result.extractData!![0].strLeague5}\n"}"
                        }

//                            "${result.extractData!![0].strLeague}\n${result.extractData!![0].strLeague2}\n${result.extractData!![0].strLeague3}\n${result.extractData!![0].strLeague4}\n${result.extractData!![0].strLeague5}"

                        println("${result.extractData!![0].strLeague} ${result.extractData!![0].strLeague2} ${result.extractData!![0].strLeague3} ${result.extractData!![0].strLeague4} " +
                                "${result.extractData!![0].strLeague5}")

                        webSiteTeam?.text = result.extractData!![0].strWebsite
                        Glide
                            .with(context).load(result.data[0].strTeamJersey).into(jerseyTeam!!)

                        if (result.extractData!![0].strInstagram == null || result.extractData!![0].strInstagram.isEmpty()) {
                            instagramTeam?.visibility = GONE
                        } else {
                            instagramTeam?.visibility = VISIBLE
                        }
                        if (result.extractData!![0].strTwitter == null || result.extractData!![0].strTwitter.isEmpty()) {
                            twitterTeam?.visibility = GONE
                        } else {
                            twitterTeam?.visibility = VISIBLE
                        }

                        if (result.extractData!![0].strYoutube == null || result.extractData!![0].strYoutube.isEmpty()) {
                            youtubeTeam?.visibility = GONE
                        } else {
                            youtubeTeam?.visibility = VISIBLE
                        }

                        if (result.extractData!![0].strFacebook == null || result.extractData!![0].strFacebook.isEmpty()) {
                            facebookTeam?.visibility = GONE
                        } else {
                            facebookTeam?.visibility = VISIBLE
                        }

                        instagramTeam?.setOnClickListener {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data =
                                Uri.parse("https://" + result.extractData!![0].strInstagram)
                            context.startActivity(intent)
                        }

                        twitterTeam?.setOnClickListener {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data = Uri.parse("https://" + result.extractData!![0].strTwitter)
                            context.startActivity(intent)
                        }

                        youtubeTeam?.setOnClickListener {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data = Uri.parse("https://" + result.extractData!![0].strYoutube)
                            context.startActivity(intent)
                        }

                        facebookTeam?.setOnClickListener {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data =
                                Uri.parse("https://" + result.extractData!![0].strFacebook)
                            context.startActivity(intent)
                        }

                    }
                }
            })

            try {
                mDialogDetail.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

            btnBookAppt!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mDialogDetail.dismiss()
                    mDialogDetail.cancel()
                }
            })

            mDialogDetail.show()
        }
    }
}