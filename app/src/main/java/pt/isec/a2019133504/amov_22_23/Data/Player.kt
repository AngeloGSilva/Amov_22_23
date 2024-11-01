package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.BitmapSerializer
import pt.isec.a2019133504.amov_22_23.Data.Deserializers._Bitmap
import pt.isec.a2019133504.amov_22_23.Data.Deserializers._Instant
import pt.isec.a2019133504.amov_22_23.Data.Messages.Message
import java.io.*
import java.net.Socket
import java.security.Timestamp
import java.time.Instant
import java.util.*
import kotlin.concurrent.thread

@Serializable
class Player(val uid : String,val nome:String , val Imagem: _Bitmap,@Transient val socket: Socket? = null){
    var Pontos : Int = 0
    var NrBoard : Int = 0
    var Timestamp : _Instant = Instant.MAX
    var TimePlayed : Long = 0
    var Lost = false

    fun assignScore(pontos : Int, segundos : Int) {
        Pontos += pontos
        Timestamp = Timestamp.plusSeconds(segundos.toLong())
        NrBoard++
    }

    fun hasLost(msplayed : Long) {
        TimePlayed = msplayed/1000;
        Lost = true
    }

    fun sendMessage(msg: Message){
        msg.sendTo(socket!!)
    }

    fun receiveMessage() : Message {
        return Message.receive(socket!!)
    }
}

