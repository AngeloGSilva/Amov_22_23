package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import android.net.Uri
import java.io.InputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket

data class Player(val Imagem:Bitmap,val nome:String,val socket: Socket){
    var Pontos:Int = 0
    var NrBoard:Int = 0
    var Timestamp:Long = 0

     val inputstream: InputStream?
        get() = socket?.getInputStream()
     val outputstream: OutputStream?
        get() = socket?.getOutputStream()

    fun send(){}
}

