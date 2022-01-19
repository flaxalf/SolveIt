package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MatchmakingProxy (private val username: String) {
    private val address = Constants.CLOUD_ADDRESS + "matchingMulti"


    /*
    lateinit var timer : Timer
    inner class MyTimer: TimerTask() {
        override fun run() {
            val reply = waitSecondPlayer()
            callback(reply["lat"].toString(),reply["lon"].toString())
            //Log.i("POLL",reply["lat"].toString())
        }

    }

    fun start(){
        timer=Timer("poll", true)
        timer.scheduleAtFixedRate(MyTimer(),0,howOften)
    }
    fun pause(){
        timer.cancel()
    }

    */
    fun hostMatch(): String {
        val url = URL("$address?user=$username")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                val reply = JSONObject(InputStreamReader(inputStream).readText())
                return reply.getString("id")
            }
        } catch (e: Exception) {
            Log.v("HOST", e.toString())
            return ""
        }
    }

    fun waitSecondPlayer(id : String): JSONObject {
        val url = URL("$address?id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())
            }
        } catch (e: Exception) {
            Log.v("HOST", e.toString())
            return JSONObject()
        }
    }

    fun joinMatch(id : String): JSONObject {
        val url = URL("$address?user=$username&id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "POST"
                return JSONObject(InputStreamReader(inputStream).readText())
            }
        } catch (e: Exception) {
            Log.v("HOST", e.toString())
            return JSONObject()
        }
    }


}