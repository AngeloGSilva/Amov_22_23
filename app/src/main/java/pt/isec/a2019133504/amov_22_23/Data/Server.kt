package pt.isec.a2019133504.amov_22_23.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.BitmapSerializer
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server {
    enum class State {
        WAITING_CONNECTIONS,PLAYING,INTERVAL,GAMEOVER
    }
    enum class MsgTypes {
        MOVE_ROW, MOVE_COL, PLAYERINFO, GAMESTART, RESULT
    }

    private val _state = MutableLiveData(State.WAITING_CONNECTIONS)
    val state : LiveData<State>
        get() = _state

    companion object {
        const val SERVER_PORT = 9999
        const val MOVE_L = -1
        const val MOVE_C = -1

        fun sendToServer(socket : Socket, msg: String) {
            thread {
                try {
                    val printStream = PrintStream(socket.getOutputStream())
                    printStream.println(msg)
                    printStream.flush()
                } catch (_: Exception) {
                }
            }
        }
    }

    var NivelAtual : Int = 0
    lateinit var level : Level
    lateinit var boards : Array<Board>
    private var socket: ServerSocket = ServerSocket(SERVER_PORT)

    var players : ArrayList<Player> = ArrayList()
        get() = field

    var playersLD = MutableLiveData<ArrayList<Player>>(players)

    init {
        thread {
            socket.run {
                System.out.println("THREAD RUNNING")
                try {
                    while(state.value == State.WAITING_CONNECTIONS) {
                        val socketClient = socket.accept()

                        thread {
                            try {
                                val bufI = socketClient.getInputStream()
                                val s = bufI.bufferedReader().readLine()
                                val json = JSONObject(s)
                                val foto2 = json.get("UserPhoto")
                                val usernameholder = json.get("Username")
                                val player: Player = Player(Json.decodeFromString(BitmapSerializer, foto2.toString()), usernameholder as String, socketClient)
                                players.add(player)
                                playersLD.postValue(players)
                                startServerComm(player)
                            } catch (_: Exception) {

                            }
                        }
                        //System.out.println("Conectado ao socket" + socketClient.toString())
                        //players[players.size] = Player(Recebido por JSON,socketClient)
                    }
                } catch (_: Exception) {
                    //_connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    //Acabar o jogo para todos
                    //serverSocket?.close()
                    //stopGame()
                }
            }
        }
    }

    private fun startServerComm(player: Player) {
        try {
            while (true) {
                var _json: JSONObject? = player.receiveJson()
                when (Json.decodeFromString<MsgTypes>(_json!!.getString("type"))) {
                    MsgTypes.MOVE_COL -> {
                        val col : Int = _json.getInt("val")
                        val res : Int = boards[player.NrBoard].getResColuna(col)
                        val json : JSONObject = JSONObject()
                        json.put("type", MsgTypes.RESULT)
                        json.put("res", res)
                        player.sendJson(json)
                        player.assignScore(res)
                    }
                    MsgTypes.MOVE_ROW -> {
                        val row : Int = _json.getInt("val")
                        val res : Int = boards[player.NrBoard].getResLinha(row)
                        val json : JSONObject = JSONObject()
                        json.put("type", MsgTypes.RESULT)
                        json.put("res", res)
                        player.sendJson(json)
                        player.assignScore(res)
                    }
                    else -> {}
                }
            }
        } catch (x: Exception) {
            System.err.println(x.message)

        } finally {
            //stopGame()
        }
    }

    fun StartGame() : Boolean {
        if(players.size <=1)
            return false
        boards = Array(10) { board -> Board.fromLevel(Level.get(NivelAtual))}
        _state.postValue(State.PLAYING)
        var json : JSONObject = JSONObject()
        json.put("type", Json.encodeToString(MsgTypes.GAMESTART))
        json.put("players", Json.encodeToString(players))
        json.put("boards", Json.encodeToString(boards))
        json.put("level", Json.encodeToString(Level.get(NivelAtual)))

        for(p in players)
            p.sendJson(json)

        return true
    }

}