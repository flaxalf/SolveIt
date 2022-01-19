package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LeaderboardProxy {
    private val address = Constants.CLOUD_ADDRESS + "leaderboard"

    fun getOrderedLeaderboard(): JSONObject {
        val url = URL(address)
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())
            }
        } catch (e: Exception) {
            Log.v("Leaderboard", e.toString())
            return JSONObject()
        }
    }
}