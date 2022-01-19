package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EndgameProxy () {
    private val address = Constants.CLOUD_ADDRESS + "endGameMulti"

    fun end(id : String): JSONObject {
        val url = URL("$address?id=$id")
        //val url = URL(address)
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