package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation.Count
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Messages.*
import pt.isec.a2019133504.amov_22_23.GameActivity
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

    private lateinit var level : Level
    private lateinit var boards : Array<Board>
    private lateinit var player : Player

    private var timerTransition : CountDownTimer? = null

    var players : MutableMap<String, Player> = mutableMapOf()
    var playersLD = MutableLiveData(players)
    var boardLD = MutableLiveData<Board>()
    var TimerTransition = MutableLiveData<Int>()

    var server : Server? = null
    private lateinit var socket : Socket

    fun startServer() {
        server = Server()
    }

    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = Server.SERVER_PORT) {
        socket = Socket()
        players.clear()
        _state.postValue(State.WAITING_START)
        thread {
            try {
                socket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                val bitmap : Bitmap
                if (CurrentUser.imgdata == null)
                    bitmap = ResourcesCompat.getDrawable(c.resources, R.drawable.ic_no_pic,null)!!.toBitmap()
                else
                    bitmap = Bitmap.createScaledBitmap(CurrentUser.imgdata!!,64,64,false)
                Message.PlayerConnect(CurrentUser.uid!!, CurrentUser.username, bitmap).sendTo(socket)
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
                            player = msg.players[CurrentUser.uid]!!
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
                                    if(player.NrBoard >= boards.size)
                                        _state.postValue(State.WAITING_FOR_NEXT_LEVEL)
                                    else {
                                        _state.postValue(State.WAITING_FOR_MOVE)
                                        updateBoard()
                                    }
                                }
                            }
                            playersLD.postValue(players)
                        }
                        is Message.GameOver -> {
                            _state.postValue(State.GAME_OVER)
                            socket.close()
                        }
                        is Message.LevelTransition ->{
                            try {
                                timerTransition?.cancel()
                            }catch (e: Exception){

                            }
                            TimerTransition.postValue((msg.time).toInt())
                            /*timerTransition =  object : CountDownTimer(msg.time, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    TimerTransition.postValue((millisUntilFinished/1000).toInt())
                                }
                                override fun onFinish() {
                                    TimerTransition.postValue(-1)
                                }
                            }.start()*/
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
            _state.postValue(State.WAITING_FOR_RESULT)
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
        }
    }

}