package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LevelTwoProxy (private val username: String, private val id: String) {
    private val address = Constants.CLOUD_ADDRESS + "levelTwoMulti"

    fun getOtherPlayerChoice(): JSONObject {
        val url = URL("$address?user=$username&id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())
            }
        } catch (e: Exception) {
            Log.v("getOtherPlayerChoice", e.toString())
            return JSONObject()
        }
    }


    fun selectNumber(number: String): JSONObject {
        val url = URL("$address?user=$username&id=$id&number=$number")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "POST"
                return JSONObject(InputStreamReader(inputStream).readText())
            }
        } catch (e: Exception) {
            Log.v("selectNumber", e.toString())
            return JSONObject()
        }
    }

}