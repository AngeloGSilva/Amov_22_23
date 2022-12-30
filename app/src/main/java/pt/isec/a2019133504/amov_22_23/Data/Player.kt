package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.BitmapSerializer
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.IntRangeSerializer
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread

typealias _Bitmap = @Serializable(BitmapSerializer::class) Bitmap

@Serializable
class Player(val Imagem:_Bitmap,val nome:String,@Transient val socket: Socket? = null){
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

