package it.sapienza.solveit.ui.proxy

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MatchmakingProxy (val username: String) {
/*
    inner class MyTimer:TimerTask() {
        override fun run() {
            val reply = doPoll()
            callback(reply["lat"].toString(),reply["lon"].toString())
            //Log.i("POLL",reply["lat"].toString())
        }

    }
    lateinit var timer : Timer
    fun start(){
        timer=Timer("poll", true)
        timer.scheduleAtFixedRate(MyTimer(),0,howOften)
    }
    fun pause(){
        timer.cancel()
    }

 */
    private val address = Constants.CLOUD_ADDRESS + "matchingMulti"

    init {

    }

    fun hostMatch(): String {
        val url = URL(address+"?user=$username")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            //Log.i("POLL", "ok")
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
}