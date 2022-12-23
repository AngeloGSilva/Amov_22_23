package pt.isec.a2019133504.amov_22_23.Data

import java.io.InputStream
import java.io.OutputStream
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

    enum class ConnectionState {
        SETTING_PARAMETERS, SERVER_CONNECTING, CLIENT_CONNECTING, CONNECTION_ESTABLISHED,
        CONNECTION_ERROR, CONNECTION_ENDED
    }

    private var socket: Socket? = null
    private val socketI: InputStream?
        get() = socket?.getInputStream()
    private val socketO: OutputStream?
        get() = socket?.getOutputStream()

    private var serverSocket: ServerSocket? = null

    private var threadComm: Thread? = null



    fun startServer() {
        if (serverSocket != null || socket != null ||
            _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        _connectionState.postValue(ConnectionState.SERVER_CONNECTING)

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            serverSocket?.run {
                try {
                    val socketClient = serverSocket!!.accept()
                    startComm(socketClient)
                } catch (_: Exception) {
                    _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    serverSocket?.close()
                    serverSocket = null
                }
            }
        }
    }

    fun startClient(serverIP: String,serverPort: Int = SERVER_PORT) {
        if (socket != null || _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        thread {
            _connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP,serverPort),5000)
                startComm(newsocket)
            } catch (_: Exception) {
                _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                stopGame()
            }
        }
    }

}