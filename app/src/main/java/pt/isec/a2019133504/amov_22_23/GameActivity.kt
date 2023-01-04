package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.Spanned
import android.util.Patterns
import android.view.Gravity
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import org.checkerframework.checker.units.qual.Current
import pt.isec.a2019133504.amov_22_23.Data.*
import pt.isec.a2019133504.amov_22_23.Data.Facts.facts
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.adapters.ConnectedPlayersAdapter
import pt.isec.a2019133504.amov_22_23.adapters.LeaderboardAdapter
import pt.isec.a2019133504.amov_22_23.databinding.ActivityGameBinding
import java.util.*


class GameActivity : AppCompatActivity(), BoardView.OnTouchListener {
    private val user = CurrentUser
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

    override fun onCellTouched(row: Int, col: Int) {
        model.updateSelectedCell(row, col)
    }

    private val model: MultiPlayer by viewModels()

    private var dlg: AlertDialog? = null
    private var ConnectedPlayerListView : ListView? = null
    private var connectedPlayersAdapter : ConnectedPlayersAdapter? = null

    private var LeaderBoardPlayerAdapter : LeaderboardAdapter? = null


    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.boardGame.registerListener(this)

        when (intent.getIntExtra("mode", SERVER_MODE)) {
            SERVER_MODE -> startAsServer()
            CLIENT_MODE -> startAsClient()
        }

        model.TimerTransition.observe(this){
             object : CountDownTimer(it.toLong(), 1000) {
                 private var toast : Toast? = null
                override fun onTick(millisUntilFinished: Long) {
                        toast?.cancel()
                        toast = Toast.makeText(applicationContext,"Starting in:" + millisUntilFinished/1000,Toast.LENGTH_SHORT)
                        toast!!.show()}
                override fun onFinish() {
                        toast?.cancel()
                        toast = Toast.makeText(applicationContext,"Starting soon",Toast.LENGTH_SHORT)
                        toast?.show()}

            }.start()
        }

        val timer = Timer()
        model.state.observe(this){
            when(it){
                MultiPlayer.State.WAITING_FOR_MOVE->{
                    dlg?.dismiss()
                    binding.boardGame.isVisible = true
                    binding.WaitingforHost.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.WaitingforPlayers.isVisible = false
                    binding.RandomFactView.isVisible=false
                }
                MultiPlayer.State.WAITING_FOR_NEXT_LEVEL ->{
                    binding.boardGame.isVisible = false
                    binding.progressBar.isVisible = true
                    binding.WaitingforHost.isVisible = false
                    binding.WaitingforPlayers.isVisible = true
                    binding.RandomFactView.text = getRandomFact()
                    binding.RandomFactView.isVisible=true

                }
                MultiPlayer.State.SPECTATING ->  {
                    binding.boardGame.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.WaitingforHost.isVisible = false
                    binding.WaitingforPlayers.isVisible = false
                }
                MultiPlayer.State.GAME_OVER ->{
                    binding.boardGame.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.WaitingforHost.isVisible = false
                    binding.WaitingforPlayers.isVisible = false
                    timer.cancel()
                    //TODO botão para o inicio
                }
                else -> {}
            }
        }

        LeaderBoardPlayerAdapter = LeaderboardAdapter(model.players,this)
        binding.leaderboard.adapter = LeaderBoardPlayerAdapter

        model.playersLD.observe(this) {
            LeaderBoardPlayerAdapter!!.notifyDataSetInvalidated()
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!model.state.value!!.equals(MultiPlayer.State.WAITING_START))
                    this@GameActivity.runOnUiThread {
                        LeaderBoardPlayerAdapter!!.notifyDataSetInvalidated()
                    }
            }
        }, 0, 1000)

        model.boardLD.observe(this) {
            updateCells(it)
        }
    }

    private fun startAsServer() {
        model.startServer()
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        //val ip = NetworkCallback.FLAG_INCLUDE_LOCATION_INFO
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )


        val llh = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            setBackgroundColor(Color.rgb(240, 224, 208))
            orientation = LinearLayout.VERTICAL
            addView(LinearLayout(context).apply {
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
            })
            addView(LinearLayout(context).apply {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = params
                setBackgroundColor(Color.rgb(240, 224, 208))
                orientation = LinearLayout.HORIZONTAL
                addView(Button(context).apply {
                    val paramsbt = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams = paramsbt
                    text = "Start"
                    textSize = 10f
                    setOnClickListener {
                        model.server!!.StartGame()
                        dlg?.setCancelable(false)
                    }
                })
                addView(Button(context).apply {
                    val paramsbt = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams = paramsbt
                    text = "Share"
                    textSize = 10f
                    setOnClickListener {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, strIPAddress);
                        startActivity(
                            Intent.createChooser(
                                shareIntent,
                                "getString(R.string.send_to)"
                            )
                        )
                    }
                })
            })


            ConnectedPlayerListView = ListView(context).apply {
                    val paramsLV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                    layoutParams = paramsLV
                    connectedPlayersAdapter = ConnectedPlayersAdapter(model.server!!.playerList.players, context)
                    adapter = connectedPlayersAdapter
                }
                addView(ConnectedPlayerListView)
        }
        dlg = AlertDialog.Builder(this)
            .setTitle("Server Mode - " + strIPAddress)
            .setView(llh)
            .setOnCancelListener {
                //model.stopServer()
                finish()
            }
            .create()

        val window = dlg!!.window
        val windowParam = window?.attributes
        windowParam?.gravity = Gravity.TOP
        window?.attributes = windowParam

        model.startClient(this.applicationContext,"localhost")
        dlg?.show()

        model.server!!.playerList.playersLD.observe(this) {
            connectedPlayersAdapter!!.notifyDataSetInvalidated()
        }
    }


    private fun updateCells(board: Board) {
        binding.boardGame.updateBoard(board)
    }

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
                    model.startClient(this.applicationContext,strIP)
                }
            }.setNeutralButton(R.string.btn_emulator) { _: DialogInterface, _: Int ->
                model.startClient(this.applicationContext,"10.0.2.2", Server.SERVER_PORT - 1)
                binding.WaitingforHost.isVisible = true
                binding.progressBar.isVisible = true
            }.setNegativeButton(R.string.button_cancel) { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(false)
            .setView(edtBox)
            .create()

        dlg.show()
    }

    fun getRandomFact(): String {
        val random = Random()
        //TODO PRTUGES
        val randomIndex = random.nextInt(facts.size)
        return "Fact: " + facts[randomIndex]
    }
}