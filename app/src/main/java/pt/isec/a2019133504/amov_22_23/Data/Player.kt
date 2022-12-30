package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread

data class Player(val Imagem:Bitmap,val nome:String,val socket: Socket){
    var Pontos:Int = 0
    var NrBoard:Int = 0
    var Timestamp:Long = 0

     val inputstream: InputStream?
        get() = socket?.getInputStream()
     val outputstream: OutputStream?
        get() = socket?.getOutputStream()

    fun sendJson(json: JSONObject){
        thread {
            try {
                val printStream = PrintStream(outputstream)
                printStream.println(json.toString())
                printStream.flush()
            } catch (_: Exception) {
                //stopGame()
            }
        }
    }

    fun receiveJson() : JSONObject? {
        val bufferedReader = inputstream?.bufferedReader()
        if (bufferedReader != null) {
            return JSONObject(bufferedReader.readLine())
        }
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getJsonObject() : JSONObject{
        var json = JSONObject()
        var baos = ByteArrayOutputStream()
        Bitmap.createScaledBitmap(Imagem,64,64,false).compress(Bitmap.CompressFormat.PNG, 90, baos)
        json.put("nome", nome)
        json.put("Imagem", Base64.getEncoder().encodeToString(baos.toByteArray()))
        return json
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun getJsonArray(p : ArrayList<Player>) : JSONArray {
            val jsonArray = JSONArray()
            for(player in p){
                jsonArray.put(player.getJsonObject())
            }

            return jsonArray
        }
    }
}

