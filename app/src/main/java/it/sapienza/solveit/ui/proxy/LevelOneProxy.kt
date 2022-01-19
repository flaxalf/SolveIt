package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LevelOneProxy {
    private val address = Constants.CLOUD_ADDRESS + "levelOneMulti"

    //GET send user and id -> reply: {button: x, success: y}
    fun getOtherPlayerChoice(id : String, username: String): JSONObject {
        val url = URL("$address?id=$id&user=$username")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())        //{button: x, success: y}
            }
        } catch (e: Exception) {
            Log.v("HOST", e.toString())
            return JSONObject()
        }
    }

    //POST send id, user, button and right (true/false) -> reply: {"POST": "OK"}
    fun sendMyChoice(id : String, username: String, button: Int, right: Boolean): JSONObject {
        val url = URL("$address?id=$id&user=$username&button=$button&right=$right")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "POST"
                return JSONObject(InputStreamReader(inputStream).readText())        //{POST: OK}
            }
        } catch (e: Exception) {
            Log.v("HOST", e.toString())
            return JSONObject()
        }
    }
}