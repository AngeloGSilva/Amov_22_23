package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.checkerframework.checker.units.qual.s
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import pt.isec.a2019133504.amov_22_23.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import java.util.*
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

    var foto = MutableLiveData<Bitmap>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun startServer() {
        if ( _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        _connectionState.postValue(ConnectionState.SERVER_CONNECTING)

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            System.out.println(serverSocket)
            serverSocket?.run {
               System.out.println("THREAD RUNNING")
                try {
                    val socketClient = serverSocket!!.accept()
                    startServerComm(socketClient)
                    //System.out.println("Conectado ao socket" + socketClient.toString())
                    //players[players.size] = Player(Recebido por JSON,socketClient)

                } catch (_: Exception) {
                    _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    //Acabar o jogo para todos
                    //serverSocket?.close()

                    //stopGame()
                }
            }
        }
        //startClient("localhost")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = SERVER_PORT) {
        if (_connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return
        thread {
            _connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                //TODO IMAGEM e Username
                val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(c.contentResolver,ProfileActivity.imgdata)

                var baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
                val json = JSONObject()
                json.put("Username", ProfileActivity.username)
                json.put("UserPhoto", Base64.getEncoder().encodeToString(baos.toByteArray()))
                System.out.println(json.toString())
                OutputStreamWriter(
                    newsocket.getOutputStream(),
                    StandardCharsets.UTF_8
                ).use { out -> out.write(json.toString()) }
                //startJogadorComm()
            } catch (_: Exception) {
                System.out.println("ERRO AO CONECTAR AO SERVIDOR")
                _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                //stopGame()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startServerComm(newSocket: Socket) {
        if (threadComm != null)
            return

        threadComm = thread {
            try {
                if (newSocket.getInputStream() == null)
                    return@thread

                //_connectionState.postValue(ConnectionState.CONNECTION_ESTABLISHED)
                val bufI = newSocket.getInputStream()
                var s : String
                bufI.bufferedReader().use { s = it.readText() }

                var json = JSONObject(s)
                //DESCODIFICAR O JSON
                var bais : ByteArrayInputStream
                var foto2 = json.get("UserPhoto")
                val decoder = Base64.getDecoder().decode(foto2.toString())
                //val decodedString = String(decoder)
                var bitmap = BitmapFactory.decodeByteArray(decoder,0,decoder.size)
                foto.postValue(bitmap)
                //var decodedbitmap = BitmapFactory.decodeByteArray(decoded,0,decoded.size)

                while (_state.value != State.GAME_OVER) {
                    //val message = bufI.

                //
                }
            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }
    private fun startJogadorComm(p :Player) {
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

}