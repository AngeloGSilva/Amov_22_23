package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import android.net.Uri
import org.json.JSONObject
import java.io.InputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

data class Player(val Imagem:Bitmap,val nome:String,val socket: Socket){
    var Pontos:Int = 0
    var NrBoard:Int = 0
    var Timestamp:Long = 0

     val inputstream: InputStream?
        get() = socket?.getInputStream()
     val outputstream: OutputStream?
        get() = socket?.getOutputStream()

    fun sendJson(json: JSONObject){
        OutputStreamWriter(
            socket.getOutputStream(),
            StandardCharsets.UTF_8
        ).use { out -> out.write(json.toString()) }
    }
}

