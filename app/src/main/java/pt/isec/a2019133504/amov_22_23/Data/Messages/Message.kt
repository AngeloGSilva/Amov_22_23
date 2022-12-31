package pt.isec.a2019133504.amov_22_23.Data.Messages

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.Level
import pt.isec.a2019133504.amov_22_23.Data.Player

@Serializable
class Message(val type : MessageTypes, val payload : String) {
    inline fun <reified T : MessagePayload> getPayload() : T {
        return Json.decodeFromString(payload)
    }

    override fun toString() : String {
        return Json.encodeToString(this)
    }

    companion object {
        inline fun <reified T : MessagePayload> create(type : MessageTypes, _payload: T) : Message {
            return Message(type, Json.encodeToString(_payload))
        }
    }
}

@Serializable
open class MessagePayload()
@Serializable
data class GameStart(val players : List<Player>, val board: List<Board>, val level: Level) : MessagePayload()
@Serializable
data class Result(val res : Int) : MessagePayload()
@Serializable
data class Move_Row(val move : Int) : MessagePayload()
@Serializable
data class Move_Col(val move : Int) : MessagePayload()

