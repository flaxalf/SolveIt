package it.sapienza.solveit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.sapienza.solveit.R
import java.util.*
import kotlin.collections.ArrayList

class LeaderboardActivity : AppCompatActivity() {

    // TODO: take from DB
    private  var personNames = ArrayList(listOf("Person 1", "Person 2", "Person 3", "Person 4", "Person 5", "Person 6", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7", "Person 7"))

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
        val leadAdapter = LeaderboardAdapter(this@LeaderboardActivity, personNames)
        recyclerView.adapter = leadAdapter
    }
}