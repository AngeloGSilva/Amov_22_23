package pt.isec.a2019133504.amov_22_23.Data.Messages

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.Deserializers._Bitmap
import pt.isec.a2019133504.amov_22_23.Data.Deserializers._Instant
import pt.isec.a2019133504.amov_22_23.Data.Level
import pt.isec.a2019133504.amov_22_23.Data.Player
import java.io.*
import java.net.Socket


@Serializable
sealed class Message {
    @Serializable
    data class StartLevel(val players : Map<String, Player>, val board: List<Board>, val level: Level) : Message()
    @Serializable
    data class Move_Row(val move : Int, val BoardN : Int) : Message()
    @Serializable
    data class Move_Col(val move : Int, val BoardN : Int) : Message()
    @Serializable
    data class PlayerUpdate(val uid:String, val Pontos:Int, val NrBoard:Int, val Timestamp: _Instant) : Message()
    @Serializable
    data class PlayerConnect(val uid:String, val nome:String, val Imagem: _Bitmap) : Message()
    @Serializable
    data class LevelTransition(val time : Long) : Message()
    @Serializable
    class GameOver : Message()

    fun sendTo(socket: Socket) {
        val outBuffer = socket.getOutputStream().bufferedWriter()
        outBuffer.write(Json.encodeToString(this))
        outBuffer.newLine()
        outBuffer.flush()
    }

    fun sendTo(outBuffer: BufferedWriter) {
        outBuffer.write(Json.encodeToString(this))
        outBuffer.newLine()
        outBuffer.flush()
    }

    companion object {
        fun receive(socket: Socket): Message {
            val inBuffer = socket.getInputStream().bufferedReader()
            return Json.decodeFromString(inBuffer.readLine())
        }

        fun receive(inBuffer: BufferedReader): Message {
            return Json.decodeFromString(inBuffer.readLine())
        }
    }
}

