package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class MultiPlayer() {
    companion object {
        const val SERVER_PORT = 9999
        const val MOVE_L = -1
        const val MOVE_C = -1
    }

    enum class State {
        STARTING, PLAYING_BOTH, PLAYING_ME, PLAYING_OTHER, ROUND_ENDED, GAME_OVER
    }

    enum class ConnectionState {
        SETTING_PARAMETERS, SERVER_CONNECTING, CLIENT_CONNECTING, CONNECTION_ESTABLISHED,
        CONNECTION_ERROR, CONNECTION_ENDED
    }

    private val _state = MutableLiveData(State.STARTING)
    val state : LiveData<State>
        get() = _state

    private val _connectionState = MutableLiveData(ConnectionState.SETTING_PARAMETERS)

    val connectionState : LiveData<ConnectionState>
        get() = _connectionState


    private lateinit var serverSocket: ServerSocket

    private var threadComm: Thread? = null

    private lateinit var players : Array<Player>

    fun startServer() {
        if ( _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        _connectionState.postValue(ConnectionState.SERVER_CONNECTING)

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            System.out.println(serverSocket)
            serverSocket?.run {
                System.out.println("IP:")
               System.out.println("THREAD RUNNING")
                try {
                    val socketClient = serverSocket!!.accept()
                    System.out.println("Conectado ao socket" + socketClient.toString())
                //startServerComm(socketClient)
                } catch (_: Exception) {
                    _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    //Acabar o jogo para todos
                    serverSocket?.close()

                    //stopGame()
                }
            }
        }
        //startClient("localhost")
    }

    //players[players.size] = Player(ProfileActivity.imgdata,ProfileActivity.username,socketClient)

    @SuppressLint("SuspiciousIndentation")
    fun startClient(serverIP: String, serverPort: Int = SERVER_PORT) {
        if (_connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return
            _connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress("10.0.2.16",serverPort),5000)
                //var playeratual = Player(ProfileActivity.imgdata,ProfileActivity.username,newsocket)
                //TODO IMAGEM e Username
                //startJogadorComm(playeratual)
            } catch (_: Exception) {
                System.out.println("ERRO AO CONECTAR AO SERVIDOR")
                _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                //stopGame()
            }
    }

    /*private fun startJogadorComm(p :Player) {
        if (threadComm != null)
            return

        threadComm = thread {
            try {
                if (p.socket == null)
                    return@thread

                _connectionState.postValue(ConnectionState.CONNECTION_ESTABLISHED)
                val bufI = p.inputstream!!.bufferedReader()

                while (_state.value != State.GAME_OVER) {
                    val message = bufI.readLine()
                    //val move = message.toIntOrNull() ?: MOVE_NONE
                    //changeOtherMove(move)
                }
            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }

    private fun startServerComm(newSocket: Socket) {
        if (threadComm != null)
            return

        threadComm = thread {
            try {
                if (socketI == null)
                    return@thread

                _connectionState.postValue(ConnectionState.CONNECTION_ESTABLISHED)
                val bufI = socketI!!.bufferedReader()

                while (_state.value != State.GAME_OVER) {
                    val message = bufI.readLine()
                    //val move = message.toIntOrNull() ?: MOVE_NONE
                   //changeOtherMove(move)
                }
            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }*/

}