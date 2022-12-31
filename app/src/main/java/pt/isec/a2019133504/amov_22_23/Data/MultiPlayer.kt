package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.BitmapSerializer
import pt.isec.a2019133504.amov_22_23.Data.Messages.GameStart
import pt.isec.a2019133504.amov_22_23.Data.Messages.Message
import pt.isec.a2019133504.amov_22_23.Data.Messages.MessageTypes
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class MultiPlayer() : ViewModel() {
    var NivelAtual : Int = 0
    lateinit var level : Level
    lateinit var boards : Array<Board>
    lateinit var player : Player


    var players : ArrayList<Player> = ArrayList()

    var playersLD = MutableLiveData<ArrayList<Player>>()
    var boardLD = MutableLiveData<Board>()
    var pontosLD = MutableLiveData<Int>()

    var server : Server? = null
    lateinit var socket : Socket

    @RequiresApi(Build.VERSION_CODES.O)
    fun startServer() {
        server = Server()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = Server.SERVER_PORT) {
        socket = Socket()
        thread {
            try {
                socket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                val bitmap = Bitmap.createScaledBitmap(ProfileActivity.imgdata!!,64,64,false)
                var json = JSONObject()
                json.put("Username", ProfileActivity.username)
                json.put("UserPhoto",  Json.encodeToString(BitmapSerializer, bitmap))
                Server.sendToServer(socket, json.toString())
                player = Player(bitmap, ProfileActivity.username)
                startJogadorComm(player)
            } catch (_: Exception) {
                System.out.println("ERRO AO CONECTAR AO SERVIDOR")
                //stopGame()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startJogadorComm(player :Player) {
        thread {
            try {
                while(true) {
                    if (socket == null)
                        return@thread

                    val bufferedReader = socket.getInputStream().bufferedReader()
                    val line = bufferedReader.readLine()
                    val msg : Message = Json.decodeFromString(line)
                    when (msg.type) {
                        MessageTypes.GAMESTART -> {
                            val gameStart : GameStart = msg.getPayload(GameStart::class)
                            player.NrBoard = 0
                            players.clear()
                            players.addAll(gameStart.players)
                            boards = gameStart.board.toTypedArray()
                            level = gameStart.level
                            playersLD.postValue(players)
                            boardLD.postValue(boards[player.NrBoard])
                        }
                        MessageTypes.RESULT -> {
                            /*val res : Int = jsonObject.getInt("res")
                            player.assignScore(res)
                            boardLD.postValue(boards[player.NrBoard])
                            pontosLD.postValue(player.Pontos)*/
                        }
                        else -> {}
                    }
                }
            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 && col == -1) return
        var jsonObject = JSONObject()
        if (row != -1) {
            jsonObject.put("type", Json.encodeToString(Server.MsgTypes.MOVE_ROW))
            jsonObject.put("val", row)
        }
        else {
            jsonObject.put("type", Json.encodeToString(Server.MsgTypes.MOVE_COL))
            jsonObject.put("val", col)
        }
        Server.sendToServer(socket, jsonObject.toString())
    }

}