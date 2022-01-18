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

        // Host match logic
        val hostBtn = findViewById<Button>(R.id.hostBtn)
        hostBtn.setOnClickListener {
            // TODO: rest api for matchmaking
            // Get the username and send to the cloud

            val sharedPref = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
            val username = sharedPref.getString(Constants.USERNAME, "Unrecognized_username")

            if (username != null && !username.isEmpty()) {
                val proxy = MatchmakingProxy(username)
                var id : String = ""
                val idTV = findViewById<TextView>(R.id.idTV)

                GlobalScope.launch {
                    async {
                        id = proxy.hostMatch()
                        idTV.text = id
                    }
                }

                /*// Go to multi level activity
                bundle.putBoolean(Constants.IS_SINGLE, false)
                multiIntent.putExtras(bundle)
                startActivity(multiIntent)

                 */
            } else {
                Toast.makeText(this, "System error", Toast.LENGTH_SHORT).show()
            }
        }

        // Join match logic
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {
            val codeET : EditText = findViewById(R.id.codeET)

            if (codeET.length() != 8 ) {
                codeET.error = "Please enter a 8 character code!"
                return@setOnClickListener
            }
            // TODO: rest api for JOIN matchmaking
            bundle.putBoolean(Constants.IS_SINGLE, false)
            multiIntent.putExtras(bundle)
            startActivity(multiIntent)
        }
    }
}