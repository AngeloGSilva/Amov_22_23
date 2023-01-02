package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Messages.*
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread


class MultiPlayer() : ViewModel() {
    private val tag = "Multiplayer"
    enum class State {
        WAITING_START, WAITING_FOR_MOVE, WAITING_FOR_RESULT,WAITING_FOR_NEXT_LEVEL, SPECTATING,GAME_OVER
    }

    private val _state = MutableLiveData(State.WAITING_START)
    val state : LiveData<State>
        get() = _state

    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var level : Level
    private lateinit var boards : Array<Board>
    private lateinit var player : Player

    var players : MutableMap<String, Player> = mutableMapOf()
    var playersLD = MutableLiveData(players)
    var boardLD = MutableLiveData<Board>()

    var server : Server? = null
    private lateinit var socket : Socket

    fun startServer() {
        server = Server()
    }

    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = Server.SERVER_PORT) {
        socket = Socket()
        thread {
            try {
                socket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                val bitmap = Bitmap.createScaledBitmap(ProfileActivity.imgdata!!,64,64,false)
                Server.sendToServer(socket, Message.create(PlayerConnect(user!!.uid, ProfileActivity.username, bitmap)))
                startJogadorComm()
            } catch (e: Exception) {
                Log.e(tag, e.toString())
                //stopGame()
            }
        }
    }


    private fun startJogadorComm() {
        thread {
            val bufferedReader = socket.getInputStream().bufferedReader()
            while(true) {
                try {
                    val line = bufferedReader.readLine()
                    val msg : Message = Json.decodeFromString(line)
                    when (msg.type) {
                        MessageTypes.START_LEVEL -> {
                            Log.d(tag, "GAMESTART")
                            val startLevel : StartLevel = msg.getPayload()
                            players.clear()
                            players.putAll(startLevel.players)
                            player = startLevel.players[user!!.uid]!!
                            if (player.Lost)
                                _state.postValue(State.SPECTATING)
                            else {
                                boards = startLevel.board.toTypedArray()
                                level = startLevel.level
                                _state.postValue(State.WAITING_FOR_MOVE)
                                updateBoard()
                            }
                            playersLD.postValue(players)
                        }
                        MessageTypes.PLAYERUPDATE -> {
                            val playerInfo : PlayerUpdate = msg.getPayload()
                            Log.d(tag, "PlayerUpdate")
                            players.get(playerInfo.uid)!!.apply {
                                Pontos = playerInfo.Pontos
                                NrBoard = playerInfo.NrBoard
                                Timestamp = playerInfo.Timestamp
                                if (this == player) {
                                    _state.postValue(State.WAITING_FOR_MOVE)
                                    updateBoard()
                                    if(player.NrBoard >= boards.size)
                                        _state.postValue(State.WAITING_FOR_NEXT_LEVEL)
                                }
                            }
                            playersLD.postValue(players)
                        }
                        MessageTypes.PLAYERINTERVAL ->{
                            val playerInfo : PlayerInterval = msg.getPayload()
                            Log.d(tag, "PlayerInterval")

                        }
                        else -> {}
                    }
                } catch (e: Exception) {
                    Log.e(tag, e.toString())
                } finally {
                    //stopGame()
                }
            }
        }
    }

    private fun updateBoard() {
        if (player.NrBoard >= boards.size)
            boardLD.postValue(Board.empty)
        else
            boardLD.postValue(boards[player.NrBoard])
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (player.NrBoard >= boards.size) return
        if (_state.value!! != State.WAITING_FOR_MOVE) return
        if (row == -1 && col == -1) return
        val msg : Message
        if (row != -1) {
            msg = Message.create(Move_Row(row, player.NrBoard))
            Log.d(tag, "Move_Row($row, ${player.NrBoard})")
        }
        else {
            msg = Message.create(Move_Col(col, player.NrBoard))
            Log.d(tag, "Move_Col($col, ${player.NrBoard})")
        }
        Server.sendToServer(socket, msg)
        _state.postValue(State.WAITING_FOR_RESULT)
    }

}