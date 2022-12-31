package pt.isec.a2019133504.amov_22_23.Data.Messages

import kotlinx.serialization.Serializable
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.Level
import pt.isec.a2019133504.amov_22_23.Data.Player

@Serializable
open class Message(val type : MessageTypes)
@Serializable
data class GameStart(val players : List<Player>, val board: List<Board>, val level: Level) : Message(MessageTypes.GAMESTART)