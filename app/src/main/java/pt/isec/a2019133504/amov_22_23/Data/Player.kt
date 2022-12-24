package pt.isec.a2019133504.amov_22_23.Data

import java.io.InputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket

data class Player(val Imagem:Byte,val nome:String,val socket: Socket){
    var Pontos:Int = 0
    var NrBoard:Int = 0
    var Timestamp:Long = 0

    private val inputstream: InputStream?
        get() = socket?.getInputStream()
    private val outputstream: OutputStream?
        get() = socket?.getOutputStream()

    fun send(){}
}

