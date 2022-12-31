package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class MultiPlayer() : ViewModel() {
    companion object {
        const val SERVER_PORT = 9999
        const val MOVE_L = -1
        const val MOVE_C = -1
    }
    var cellsLiveData = MutableLiveData<Board>()

    enum class State {
        WAITING_CONNECTIONS,PLAYING,INTERVAL,GAMEOVER
    }

    private val _state = MutableLiveData(State.WAITING_CONNECTIONS)
    val state : LiveData<State>
        get() = _state

    private lateinit var serverSocket: ServerSocket

    var players : ArrayList<Player> = ArrayList()
        get() = field

    var playersCliente : ArrayList<Player> = ArrayList()
        get() = field

    var NivelAtual : Int = 0
    lateinit var level : Level
    lateinit var boards : Array<Board>

    private var BoardAtual : Int = 0

    var serverUsers = MutableLiveData<ArrayList<Player>>()

    var usersPlayers = MutableLiveData<ArrayList<Player>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun startServer() {
        serverSocket = ServerSocket(SERVER_PORT)
        thread {
            System.out.println(serverSocket)
            serverSocket?.run {
               System.out.println("THREAD RUNNING")
                try {
                    while(state.value == State.WAITING_CONNECTIONS) {
                        val socketClient = serverSocket!!.accept()

                        thread {
                            try {
                                val bufI = socketClient.getInputStream()
                                var s = bufI.bufferedReader().readLine()
                                var json = JSONObject(s)
                                var foto2 = json.get("UserPhoto")
                                var usernameholder = json.get("Username")
                                val player: Player = Player(Json.decodeFromString(BitmapSerializer, foto2.toString()), usernameholder as String, socketClient)
                                players.add(player)
                                serverUsers.postValue(players)
                                startServerComm(player)
                            } catch (_: Exception) {

                            }
                        }

                    }
                } catch (_: Exception) {
                } finally {
                    //serverSocket?.close()
                    //stopGame()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = SERVER_PORT) {
        thread {
            try {
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                val bitmap = Bitmap.createScaledBitmap(ProfileActivity.imgdata!!,64,64,false)
                var json = JSONObject()
                json.put("Username", ProfileActivity.username)
                json.put("UserPhoto",  Json.encodeToString(BitmapSerializer, bitmap))
                System.out.println(json.toString())
                newsocket.getOutputStream().run {
                    thread {
                        try {
                            val printStream = PrintStream(this)
                            printStream.println(json.toString())
                            printStream.flush()
                        } catch (_: Exception) {
                            //stopGame()
                        }
                    }
                }
                startJogadorComm(Player(bitmap,ProfileActivity.username,newsocket))
            } catch (_: Exception) {
                System.out.println("ERRO AO CONECTAR AO SERVIDOR")
                //stopGame()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startServerComm(player: Player) {
        try {
            while (true) {
                var _json: JSONObject? = player.receiveJson()

            }
        } catch (x: Exception) {
            System.err.println(x.message)

        } finally {
            //stopGame()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun StartGame() : Boolean {
        if(players.size <=1)
            return false
        boards = Array(10) { board -> Board.fromLevel(Level.get(NivelAtual))}
       _state.postValue(State.PLAYING)
        var json : JSONObject = JSONObject()
        json.put("type","GAMESTART")
        json.put("players", Json.encodeToString(players))
        json.put("boards", Json.encodeToString(boards))
        json.put("level", Json.encodeToString(Level.get(NivelAtual)))

        for(p in players)
            p.sendJson(json)

        return true
     }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startJogadorComm(player :Player) {
        thread {
            try {
                while(true) {
                    if (player.socket == null)
                        return@thread

                    val bufferedReader = player.inputstream!!.bufferedReader()
                    var jsonObject = JSONObject(bufferedReader.readLine())
                    when (jsonObject.get("type")) {
                        "GAMESTART" -> {
                            BoardAtual = 0
                            for (_p in Json.decodeFromString<List<Player>>(jsonObject.getString("players")))
                                playersCliente.add(_p)
                            boards = Json.decodeFromString(jsonObject.getString("boards"))
                            level = Json.decodeFromString(jsonObject.getString("level"))
                            cellsLiveData.postValue(boards[BoardAtual])
                            usersPlayers.postValue(playersCliente)
                        }
                    }
                }
            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }

}