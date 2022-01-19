package it.sapienza.solveit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.proxy.LeaderboardProxy
import it.sapienza.solveit.ui.proxy.MatchmakingProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class LeaderboardActivity : AppCompatActivity() {

    // TODO: take from DB
    lateinit var personNames : JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val homeIV = findViewById<ImageView>(R.id.boardHomeIV)
        homeIV.setOnClickListener {
            startActivity(Intent(this@LeaderboardActivity, MenuActivity::class.java))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // set a LinearLayoutManager with default vertical orientation
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager

        //  set the adapter which will send the reference and data to Adapter
        // Take the leaderboard from the cloud
        val proxy = LeaderboardProxy()
        GlobalScope.launch {
            async {
                val reply = proxy.getOrderedLeaderboard()

                runOnUiThread {
                    run() {
                        val leadAdapter = LeaderboardAdapter(this@LeaderboardActivity, reply)
                        recyclerView.adapter = leadAdapter
                    }
                }
            }
        }
    }
}