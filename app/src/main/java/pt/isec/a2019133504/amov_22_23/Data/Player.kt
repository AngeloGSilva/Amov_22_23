package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import android.net.Uri
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
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
}

