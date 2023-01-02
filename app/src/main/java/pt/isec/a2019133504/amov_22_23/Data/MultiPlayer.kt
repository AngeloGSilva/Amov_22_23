package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Messages.*
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import pt.isec.a2019133504.amov_22_23.R
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
                val bitmap : Bitmap
                if (ProfileActivity.imgdata == null)
                    bitmap = ResourcesCompat.getDrawable(c.resources, R.drawable.ic_no_pic,null)!!.toBitmap()
                else
                    bitmap = Bitmap.createScaledBitmap(ProfileActivity.imgdata!!,64,64,false)
                Message.PlayerConnect(user!!.uid, ProfileActivity.username, bitmap).sendTo(socket)
                startJogadorComm()
            } catch (e: Exception) {
                Log.e(tag, e.stackTraceToString())
                //stopGame()
            }
        }
    }


    private fun startJogadorComm() {
        thread {
            val bufferedReader = socket.getInputStream().bufferedReader()
            while(true) {
                try {
                    val msg = Message.receive(bufferedReader)
                    when (msg) {
                        is Message.StartLevel -> {
                            Log.d(tag, "GAMESTART")
                            players.clear()
                            players.putAll(msg.players)
                            player = msg.players[user!!.uid]!!
                            if (player.Lost)
                                _state.postValue(State.SPECTATING)
                            else {
                                boards = msg.board.toTypedArray()
                                level = msg.level
                                _state.postValue(State.WAITING_FOR_MOVE)
                                updateBoard()
                            }
                            playersLD.postValue(players)
                        }
                        is Message.PlayerUpdate -> {
                            Log.d(tag, "PlayerUpdate")
                            players.get(msg.uid)!!.apply {
                                Pontos = msg.Pontos
                                NrBoard = msg.NrBoard
                                Timestamp = msg.Timestamp
                                if (this == player) {
                                    _state.postValue(State.WAITING_FOR_MOVE)
                                    updateBoard()
                                    if(player.NrBoard >= boards.size)
                                        _state.postValue(State.WAITING_FOR_NEXT_LEVEL)
                                }
                            }
                            playersLD.postValue(players)
                        }
                        is Message.GameOver -> {
                            _state.postValue(State.GAME_OVER)
                            socket.close()
                        }
                        else -> {}
                    }
                } catch (e: Exception) {
                    if (socket.isClosed)
                        break
                    Log.e(tag, e.stackTraceToString())
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
        thread {
            if (player.NrBoard >= boards.size) return@thread
            if (_state.value!! != State.WAITING_FOR_MOVE) return@thread
            if (row == -1 && col == -1) return@thread
            val msg : Message
            if (row != -1) {
                msg = Message.Move_Row(row, player.NrBoard)
                Log.d(tag, "Move_Row($row, ${player.NrBoard})")
            }
            else {
                msg = Message.Move_Col(col, player.NrBoard)
                Log.d(tag, "Move_Col($col, ${player.NrBoard})")
            }
            msg.sendTo(socket)
            _state.postValue(State.WAITING_FOR_RESULT)
        }
    }

}