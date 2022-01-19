package it.sapienza.solveit.ui.proxy

import android.util.Log
import it.sapienza.solveit.ui.models.Constants
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LevelThreeProxy () {
    private val address = Constants.CLOUD_ADDRESS + "levelThreeMulti"

    fun readCounter(id : String): JSONObject {
        val url = URL("$address?id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "GET"
                return JSONObject(InputStreamReader(inputStream).readText())        //{count: x}
            }
        } catch (e: Exception) {
            Log.v("readCounter", e.toString())
            return JSONObject()
        }
    }

    fun increaseCounter(id : String): JSONObject {
        val url = URL("$address?id=$id")
        val conn = url.openConnection() as HttpsURLConnection
        try {
            conn.run {
                requestMethod = "POST"
                return JSONObject(InputStreamReader(inputStream).readText())        //{POST: OK, count: x}
            }
        } catch (e: Exception) {
            Log.v("increaseCounter", e.toString())
            return JSONObject()
        }
    }
}