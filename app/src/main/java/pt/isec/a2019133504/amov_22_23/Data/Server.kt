package pt.isec.a2019133504.amov_22_23.Data

import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
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
    }

    var NivelAtual : Int = 0
    lateinit var level : Level
    val boards = ArrayList<Board>()
    private var socket: ServerSocket = ServerSocket(SERVER_PORT)
    val playerList = PlayerList()

    private val timer = Timer()

    init {
        thread {
            socket.use {
                System.out.println("THREAD RUNNING")
                while(state.value == State.WAITING_CONNECTIONS) {
                    try {
                        val socketClient = socket.accept()
                        thread {
                            try {
                                val msg = Message.receive(socketClient)
                                if (msg is Message.PlayerConnect){
                                    Log.d(tag, "PlayerConnect")
                                    val player = Player(msg.uid, msg.nome, msg.Imagem, socketClient)
                                    playerList.addPlayer(player)
                                    startServerComm(player)
                                }
                            } catch (e: Exception) {
                                if (!socketClient.isClosed)
                                    Log.e(tag, e.stackTraceToString())
                            }
                        }
                    //System.out.println("Conectado ao socket" + socketClient.toString())
                    //players[players.size] = Player(Recebido por JSON,socketClient)
                    } catch (e: Exception) {
                        if (!socket.isClosed)
                            Log.e(tag, e.stackTraceToString())
                    }
                }
            }
        }
    }

    private fun startServerComm(player: Player) {
        while (_state.value!! != State.GAMEOVER) {
            try {
                val msg = player.receiveMessage()
                when (msg) {
                    is Message.Move_Col -> {
                        Log.d(tag, "MOVE_COL")
                        if (msg.BoardN >= boards.size) continue
                        if (msg.BoardN != player.NrBoard) continue
                        val res : Int = boards[player.NrBoard].getResColuna(msg.move)
                        player.assignScore(res, if (res>0) Level.get(NivelAtual).winTime else 0)
                        playerList.sendToAll(
                            Message.PlayerUpdate(
                                player.uid,
                                player.Pontos,
                                player.NrBoard,
                                player.Timestamp
                            )
                        )
                    }
                    is Message.Move_Row -> {
                        if (msg.BoardN >= boards.size) continue
                        if (msg.BoardN != player.NrBoard) continue
                        val res : Int = boards[player.NrBoard].getResLinha(msg.move)
                        player.assignScore(res, if (res>0) Level.get(NivelAtual).winTime else 0)
                        playerList.sendToAll(
                            Message.PlayerUpdate(
                                player.uid,
                                player.Pontos,
                                player.NrBoard,
                                player.Timestamp
                            )
                        )
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                if (player.socket==null || player.socket.isClosed)
                    break
                if (_state.value!=State.GAMEOVER)
                    Log.e(tag, e.stackTraceToString())
            }
        }
        player.socket!!.close()
    }

    fun StartGame() {
        if(playerList.players.size <=1)
            return
        thread {
            NivelAtual = -1
            socket.close()
            NextLevel()
        }
    }

    fun EndGame() {
        //TODO pontuação db
    }

    fun NextLevel() : Boolean {
        if (NivelAtual>=0)
            playerList.markBelowThreshold(Level.get(NivelAtual).threshold)
        if (playerList.allLost() || Level.isLast(NivelAtual)) {
            _state.postValue(State.GAMEOVER)
            playerList.sendToAll(Message.GameOver())
            return false
        }
        if (NivelAtual>=0) {
            playerList.sendToAll(Message.LevelTransition(5000))
            Thread.sleep(5000)
        }
        NivelAtual++
        val level = Level.get(NivelAtual)
        boards.clear()
        boards.addAll(Array(10) { Board.fromLevel(level)})
        playerList.setTimestap(now().plusSeconds(level.maxTime.toLong()))
        playerList.setBoardNr(0)
        _state.postValue(State.PLAYING)
        playerList.sendToAll(Message.StartLevel(playerList.players, boards, Level.get(NivelAtual)))
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!playerList.allFinished(boards.size))
                    return
                NextLevel()
                this.cancel()
            }
        }, 0, 1000)
        return true
    }
}