package pt.isec.a2019133504.amov_22_23.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Messages.*
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket
import java.time.Instant.now
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class Server {
    private val tag = "Server"
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
                } catch (e: Exception) {
                    Log.e("sendToServer", e.stackTraceToString())
                }
            }
        }
    }

    var NivelAtual : Int = 0
    lateinit var level : Level
    val boards = ArrayList<Board>()
    private var socket: ServerSocket = ServerSocket(SERVER_PORT)
    val playerList = PlayerList()

    private val timer = Timer()

    init {
        thread {
            socket.run {
                System.out.println("THREAD RUNNING")
                while(state.value == State.WAITING_CONNECTIONS) {
                    try {
                        val socketClient = socket.accept()
                        thread {
                            try {
                                val bufI = socketClient.getInputStream()
                                val s = bufI.bufferedReader().readLine()
                                val msg : Message = Json.decodeFromString(s)
                                if (msg.type == MessageTypes.PLAYER_CONNECT){
                                    val playerConnect : PlayerConnect = msg.getPayload()
                                    Log.d(tag, "PlayerConnect")
                                    val player = Player(playerConnect.uid, playerConnect.nome, playerConnect.Imagem, socketClient)
                                    playerList.addPlayer(player)
                                    startServerComm(player)
                                }
                            } catch (e: Exception) {
                                Log.e(tag, e.toString())
                            }
                        }
                    //System.out.println("Conectado ao socket" + socketClient.toString())
                    //players[players.size] = Player(Recebido por JSON,socketClient)
                    } catch (e: Exception) {
                        Log.e(tag, e.stackTraceToString())
                    } finally {
                        //Acabar o jogo para todos
                        //socket.close()
                        //stopGame()
                    }
                }
            }
        }
    }

    private fun startServerComm(player: Player) {
            while (true) {
                try {
                    val msg : Message = player.receiveMessage()
                    when (msg.type) {
                        MessageTypes.MOVE_COL -> {
                            Log.d(tag, "MOVE_COL")
                            val moveCol : Move_Col = msg.getPayload()
                            if (moveCol.BoardN >= boards.size) continue
                            if (moveCol.BoardN != player.NrBoard) continue
                            val res : Int = boards[player.NrBoard].getResColuna(moveCol.move)
                            player.assignScore(res, if (res>0) Level.get(NivelAtual).winTime else 0)
                            playerList.sendToAll(Message.create(PlayerUpdate(player.uid, player.Pontos, player.NrBoard, player.Timestamp)))
                        }
                        MessageTypes.MOVE_ROW -> {
                            Log.d(tag, "MOVE_ROW")
                            val moveRow : Move_Row = msg.getPayload()
                            if (moveRow.BoardN >= boards.size) continue
                            if (moveRow.BoardN != player.NrBoard) continue
                            val res : Int = boards[player.NrBoard].getResLinha(moveRow.move)
                            player.assignScore(res, if (res>0) Level.get(NivelAtual).winTime else 0)
                            playerList.sendToAll(Message.create(PlayerUpdate(player.uid, player.Pontos, player.NrBoard, player.Timestamp)))
                        }
                        else -> {}
                    }
                } catch (e: Exception) {
                    Log.e(tag, e.stackTraceToString())
                } finally {
                    //stopGame()
                }
            }
    }

    fun StartGame() : Boolean {
        //FIXME uncomment
        /*if(players.size <=1)
            return false*/
        NivelAtual = -1
        return NextLevel()
    }

    fun NextLevel() : Boolean {
        if (!playerList.allFinished(boards.size)) return false
        Log.d("NextLevel", "Nivel atual: ${NivelAtual}")
        if (Level.isLast(NivelAtual)) TODO("game over")
        if (NivelAtual>=0)
            playerList.markBelowThreshold(Level.get(NivelAtual).threshold)
        NivelAtual++
        val level = Level.get(NivelAtual)
        boards.clear()
        boards.addAll(Array(10) { Board.fromLevel(level)})
        playerList.setTimestap(now().plusSeconds(level.maxTime.toLong()))
        playerList.setBoardNr(0)
        _state.postValue(State.PLAYING)
        playerList.sendToAll(Message.create(StartLevel(playerList.players, boards, Level.get(NivelAtual))))
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (NextLevel())
                    this.cancel()
            }
        }, 0, 1000)
        return true
    }
}