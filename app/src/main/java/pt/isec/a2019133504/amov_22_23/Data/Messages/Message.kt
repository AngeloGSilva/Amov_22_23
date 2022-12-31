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
        inline fun <reified T : MessagePayload> create(_payload: T) : Message {
            return Message(_payload.type, Json.encodeToString(_payload))
        }
    }
}

@Serializable
open class MessagePayload(@Transient val type : MessageTypes = MessageTypes.DEFAULT)
@Serializable
data class GameStart(val players : List<Player>, val board: List<Board>, val level: Level) : MessagePayload(MessageTypes.GAMESTART)
@Serializable
data class Result(val res : Int) : MessagePayload(MessageTypes.RESULT)
@Serializable
data class Move_Row(val move : Int, val BoardN : Int) : MessagePayload(MessageTypes.MOVE_ROW)
@Serializable
data class Move_Col(val move : Int, val BoardN : Int) : MessagePayload(MessageTypes.MOVE_COL)
@Serializable
data class PlayerInfo(val players : List<Player>) : MessagePayload(MessageTypes.PLAYERINFO)