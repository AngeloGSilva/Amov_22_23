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
import androidx.lifecycle.ViewModel
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread


class MultiPlayer() : ViewModel() {
    companion object {
        const val SERVER_PORT = 9999
        const val MOVE_L = -1
        const val MOVE_C = -1
    }

    enum class State {
        WAITING_CONNECTIONS,PLAYING,INTERVAL,GAMEOVER
    }

    private val _state = MutableLiveData(State.WAITING_CONNECTIONS)
    val state : LiveData<State>
        get() = _state

    private lateinit var serverSocket: ServerSocket

    private var threadComm: Thread? = null

    var players : ArrayList<Player> = ArrayList()
        get() = field

    //var usersinfo = MutableLiveData<Bitmap>()

    var testeusers = MutableLiveData<ArrayList<Player>>()


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
                        startServerComm(socketClient)
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    fun startClient(c : Context,serverIP: String, serverPort: Int = SERVER_PORT) {

        thread {
            try {
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP, serverPort), 5000)
                var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(c.contentResolver,ProfileActivity.imgdata)
                var baos = ByteArrayOutputStream()
                bitmap = Bitmap.createScaledBitmap(bitmap,64,64,false)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
                val json = JSONObject()
                json.put("Username", ProfileActivity.username)
                json.put("UserPhoto", Base64.getEncoder().encodeToString(baos.toByteArray()))
                System.out.println(json.toString())
                OutputStreamWriter(
                    newsocket.getOutputStream(),
                    StandardCharsets.UTF_8
                ).use { out -> out.write(json.toString()) }

                startJogadorComm(Player(bitmap,ProfileActivity.username,newsocket))
            } catch (_: Exception) {
                System.out.println("ERRO AO CONECTAR AO SERVIDOR")
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
                var foto2 = json.get("UserPhoto")
                //json.getJSONArray("UserPhoto")
                var usernameholder = json.get("Username")
                val decoder = Base64.getDecoder().decode(foto2.toString())
                var bitmap = BitmapFactory.decodeByteArray(decoder,0,decoder.size)

                players.add(Player(bitmap, usernameholder as String,newSocket))
                testeusers.postValue(players)


                //usersinfo.postValue(players[0].Imagem)

            } catch (x: Exception) {
                System.err.println(x.message)
            } finally {
                //stopGame()
            }
        }
    }

    fun StartGame() : Boolean {
        if(players.size <=1)
            return false
       _state.postValue(State.PLAYING)
        var json : JSONObject = JSONObject()
        json.put("type","GAMESTART")

        for(p in players){
            p.sendJson(json)
        }

        return true
     }

    private fun startJogadorComm(p :Player) {
        if (threadComm != null)
            return

        threadComm = thread {
            try {
                if (p.socket == null)
                    return@thread

                val bufI = p.inputstream!!.bufferedReader()


                    val message = bufI.readLine()


            } catch (_: Exception) {
            } finally {
                //stopGame()
            }
        }
    }

}