package pt.isec.a2019133504.amov_22_23.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.BitmapSerializer
import pt.isec.a2019133504.amov_22_23.Data.Messages.*
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server {
    enum class State {
        WAITING_CONNECTIONS,PLAYING,INTERVAL,GAMEOVER
    }

    private val _state = MutableLiveData(State.WAITING_CONNECTIONS)
    val state : LiveData<State>
        get() = _state

    companion object {
        const val SERVER_PORT = 9999
        const val MOVE_L = -1
        const val MOVE_C = -1

        fun sendToServer(socket : Socket, msg: Message) {
            thread {
                try {
                    val printStream = PrintStream(socket.getOutputStream())
                    printStream.println(msg.toString())
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

    var players : MutableMap<String, Player> = mutableMapOf()
    var playersLD = MutableLiveData(players.values)

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
                                val msg : Message = Json.decodeFromString(s)
                                if (msg.type == MessageTypes.PLAYER_CONNECT){
                                    val playerConnect : PlayerConnect = msg.getPayload()
                                    val player = Player(playerConnect.uid, playerConnect.nome, playerConnect.Imagem, socketClient)
                                    players.put(player.uid, player)
                                    playersLD.postValue(players.values)
                                    startServerComm(player)
                                }
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
                val msg : Message = player.receiveMessage()
                when (msg.type) {
                    MessageTypes.MOVE_COL -> {
                        val moveCol : Move_Col = msg.getPayload()
                        if (moveCol.BoardN > boards.size) continue
                        if (moveCol.BoardN != player.NrBoard) continue
                        val res : Int = boards[player.NrBoard].getResColuna(moveCol.move)
                        player.assignScore(res)
                        SendToAllPlayers(Message.create(PlayerUpdate(player.uid, player.Pontos, player.NrBoard, player.Timestamp)))
                    }
                    MessageTypes.MOVE_ROW -> {
                        val moveRow : Move_Row = msg.getPayload()
                        if (moveRow.BoardN > boards.size) continue
                        if (moveRow.BoardN != player.NrBoard) continue
                        val res : Int = boards[player.NrBoard].getResLinha(moveRow.move)
                        player.assignScore(res)
                        SendToAllPlayers(Message.create(PlayerUpdate(player.uid, player.Pontos, player.NrBoard, player.Timestamp)))
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

    fun SendToAllPlayers(message : Message) {
        for(p in players.values)
            p.sendMessage(message)
    }

    fun StartGame() : Boolean {
        //FIXME uncomment
        /*if(players.size <=1)
            return false*/
        boards = Array(10) { board -> Board.fromLevel(Level.get(NivelAtual))}
        _state.postValue(State.PLAYING)
        SendToAllPlayers(Message.create(GameStart(players, boards.asList(), Level.get(NivelAtual))))
        return true
    }

}