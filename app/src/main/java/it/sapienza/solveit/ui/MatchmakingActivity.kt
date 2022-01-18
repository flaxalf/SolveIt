package it.sapienza.solveit.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.LevelsActivity
import it.sapienza.solveit.ui.models.Constants
import it.sapienza.solveit.ui.proxy.MatchmakingProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.*

class MatchmakingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matchmaking)

        // Toolbar home icon
        val homeIV = findViewById<ImageView>(R.id.matchmakingHomeIV)
        homeIV.setOnClickListener {
            startActivity(Intent(this@MatchmakingActivity, MenuActivity::class.java))
        }

        val multiIntent = Intent(this@MatchmakingActivity, LevelsActivity::class.java)
        val bundle = Bundle()

        val hostBtn = findViewById<Button>(R.id.hostBtn)
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        val idTV = findViewById<TextView>(R.id.idTV)

        val sharedPref = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        val username = sharedPref.getString(Constants.USERNAME, "Unrecognized_username")


        if (username != null && username.isNotEmpty()) {
            val proxy = MatchmakingProxy(username)
            hostBtn.setOnClickListener {
                var id = ""

                GlobalScope.launch {
                    async {
                        id = proxy.hostMatch()
                        idTV.text = id
                        proxy.waitSecondPlayer(id)

                    }
                }

                val timer = Timer("wait", true)
                timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        Log.d("matching", "checking")
                        if(id.isNotEmpty()) {
                            val waitResponse = proxy.waitSecondPlayer(id)
                            if(waitResponse.getString("matching").equals("start")){

                                // Go to multi level activity
                                bundle.putBoolean(Constants.IS_SINGLE, false)
                                multiIntent.putExtras(bundle)
                                startActivity(multiIntent)
                                Log.d("matching", "matched")
                                timer.cancel()
                            }
                        }
                    }

                }, 1000, 1000)



            }
            // Join match logic
            joinBtn.setOnClickListener {
                val codeET : EditText = findViewById(R.id.codeET)

                if (codeET.length() != 8 ) {
                    codeET.error = "Please enter a 8 character code!"
                    return@setOnClickListener
                }

                GlobalScope.launch {
                    async {
                        var reply = proxy.joinMatch(codeET.text.toString()
                            .uppercase(Locale.getDefault()))

                        var postStatus = reply.getString("POST")

                        if (postStatus.equals("OK")) {
                            bundle.putBoolean(Constants.IS_SINGLE, false)
                            multiIntent.putExtras(bundle)
                            startActivity(multiIntent)
                        } else {
                            runOnUiThread {
                                run() {
                                    codeET.error = "Invalid code, try again"
                                }
                            }
                        }
                    }
                }
            }

        } else {
            Toast.makeText(this, "System error", Toast.LENGTH_SHORT).show()
        }
    }
}