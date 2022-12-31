package pt.isec.a2019133504.amov_22_23.Data.Messages

import kotlin.reflect.KClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.Level
import pt.isec.a2019133504.amov_22_23.Data.Player

@Serializable
class Message(val type : MessageTypes,@Transient val _payload: MessagePayload = MessagePayload()) {
    val payload : String = Json.encodeToString(_payload)
    inline fun <reified T : MessagePayload> getPayload(clazz: KClass<T>) : T {
        return Json.decodeFromString(payload)
    }
}

@Serializable
open class MessagePayload()
@Serializable
data class GameStart(val players : List<Player>, val board: List<Board>, val level: Level) : MessagePayload()

