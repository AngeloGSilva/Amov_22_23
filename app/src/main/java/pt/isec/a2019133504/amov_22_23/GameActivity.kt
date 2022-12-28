package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager.NetworkCallback
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.postDelayed
import pt.isec.a2019133504.amov_22_23.Data.MultiPlayer
import pt.isec.a2019133504.amov_22_23.Data.MultiPlayer.Companion.SERVER_PORT

class GameActivity : AppCompatActivity() {
    companion object {
        private const val SERVER_MODE = 0
        private const val CLIENT_MODE = 1

        fun getServerModeIntent(context : Context) : Intent {
            return Intent(context,GameActivity::class.java).apply {
                putExtra("mode", SERVER_MODE)
            }
        }

        fun getClientModeIntent(context : Context) : Intent {
            return Intent(context,GameActivity::class.java).apply {
                putExtra("mode", CLIENT_MODE)
            }
        }
    }

    private val model: MultiPlayer = MultiPlayer()
    private var dlg: AlertDialog? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        model.connectionState.observe(this) { state ->
            updateUI()
            if (state != MultiPlayer.ConnectionState.SETTING_PARAMETERS &&
                state != MultiPlayer.ConnectionState.SERVER_CONNECTING &&
                dlg?.isShowing == true) {
                dlg?.dismiss()
                dlg = null
            }

            if (state == MultiPlayer.ConnectionState.CONNECTION_ERROR ||
                state == MultiPlayer.ConnectionState.CONNECTION_ENDED ) {
                finish()
            }
        }
        model.testeusers.observe(this) {
            var imageview = findViewById<ImageView>(R.id.imagejson)
            imageview.setImageBitmap(it[0].Imagem)
            var userview = findViewById<TextView>(R.id.userjson)
            userview.setText(it[0].nome)
        }

        model.usersinfo.observe(this){
            var imageview = findViewById<ImageView>(R.id.imagejson)
            imageview.setImageBitmap(it)
        }

        when (intent.getIntExtra("mode", SERVER_MODE)) {
            SERVER_MODE -> startAsServer()
            CLIENT_MODE -> startAsClient()
        }
    }
    private fun updateUI() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAsServer() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        //val ip = NetworkCallback.FLAG_INCLUDE_LOCATION_INFO
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )

        val ll = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            setBackgroundColor(Color.rgb(240, 224, 208))
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(Color.rgb(96, 96, 32))
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.msg_ip_address))
                textSize = 20f
                setTextColor(Color.rgb(96, 96, 32))
            })
            addView(Button(context).apply {
                val paramsbt = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = paramsbt
                text = "Start"
                textSize = 10f
                setOnClickListener {
                    //Notificar o jogo de que o server começou
                    //Notifica os clientes que vai começar
                    dlg?.dismiss()
                }
            })
        }

        dlg = AlertDialog.Builder(this)
            .setTitle("Server Mode - " + strIPAddress)
            .setView(ll)
            .setOnCancelListener {
                //model.stopServer()
                finish()
            }
            .create()

        model.startServer()

        dlg?.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAsClient() {
        val edtBox = EditText(this).apply {
            maxLines = 1
            filters = arrayOf(object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    source?.run {
                        var ret = ""
                        forEach {
                            if (it.isDigit() || it == '.')
                                ret += it
                        }
                        return ret
                    }
                    return null
                }

            })
        }
        val dlg = AlertDialog.Builder(this)
            .setTitle(R.string.client_mode)
            .setMessage(R.string.ask_ip)
            .setPositiveButton(R.string.button_connect) { _: DialogInterface, _: Int ->
                val strIP = edtBox.text.toString()
                if (strIP.isEmpty() || !Patterns.IP_ADDRESS.matcher(strIP).matches()) {
                    Toast.makeText(this@GameActivity, R.string.error_address, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    model.startClient(this,strIP)
                }
            }.setNeutralButton(R.string.btn_emulator) { _: DialogInterface, _: Int ->
                model.startClient(this,"10.0.2.2", SERVER_PORT - 1)
            }.setNegativeButton(R.string.button_cancel) { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(false)
            .setView(edtBox)
            .create()

        dlg.show()
    }
}