package it.sapienza.solveit.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.LevelsActivity
import android.graphics.drawable.AnimationDrawable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import it.sapienza.solveit.ui.models.Constants
import java.util.*


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.layout)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        val animZoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        val btnSingle = findViewById<Button>(R.id.buttonSingle)
        val btnMulti = findViewById<Button>(R.id.buttonMulti)
        val btnLeaderboard = findViewById<Button>(R.id.buttonLeaderboard)

        val newIntent = Intent(this@MenuActivity, LevelsActivity::class.java)
        val matchmakingIntent = Intent(this@MenuActivity, MatchmakingActivity::class.java)
        val leaderboardIntent = Intent(this@MenuActivity, LeaderboardActivity::class.java)
        val bundle = Bundle()

        btnSingle.setOnClickListener{
            btnSingle.startAnimation(animZoom)
            bundle.putBoolean(Constants.IS_SINGLE, true)
            newIntent.putExtras(bundle)
            startActivity(newIntent)
        }

        btnMulti.setOnClickListener{
            btnMulti.startAnimation(animZoom)
            startActivity(matchmakingIntent)
        }

        btnLeaderboard.setOnClickListener{
            btnLeaderboard.startAnimation(animZoom)
            startActivity(leaderboardIntent)
        }

        // Get the username
        val sharedPref = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE)
        val username = sharedPref.getString(Constants.USERNAME, "Unrecognized_username")
            ?.replaceFirstChar(Char::titlecase)
        val welcomeTV = findViewById<TextView>(R.id.welcomeTV)
        welcomeTV.text = "Welcome $username"
    }

    // for handling back button of the Android Device
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}