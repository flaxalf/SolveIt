package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LevelOneProxy(private val id : String, private val username: String) {
    private val address = Constants.CLOUD_ADDRESS + "levelOneMulti"

    //GET send id -> reply: {success: x}
    fun getOtherPlayerChoice(): JSONObject {
        val url = URL("$address?id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())        //{success: x}
            }
        } catch (e: Exception) {
            Log.v("getOtherPlayerChoice", e.toString())
            return JSONObject()
        }
    }

    //POST send id, user, button and right (true/false) -> reply: {"POST": "OK"}
    fun sendMyChoice(right: Boolean): JSONObject {
        val url = URL("$address?id=$id&user=$username&right=$right")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "POST"
                return JSONObject(InputStreamReader(inputStream).readText())        //{POST: OK}
            }
        } catch (e: Exception) {
            Log.v("sendMyChoice", e.toString())
            return JSONObject()
        }
    }
}