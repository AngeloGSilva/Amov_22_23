package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.PlayerScore
import pt.isec.a2019133504.amov_22_23.Data.SinglePlayer
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {
    private val user = CurrentUser
    private val singlePlayer : SinglePlayer by viewModels()
    private lateinit var binding: ActivityMode1Binding

    companion object{
        val FROM_MULTIPLAYER = "FROM_MULTIPLAYER"
        val NIVEL = "NIVEL"
        val BOARD = "BOARD"
        val PONTOS = "PONTOS"
        val TEMPO = "TEMPO"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!singlePlayer.starter) {
            if (intent.getBooleanExtra(FROM_MULTIPLAYER, false)) {
                singlePlayer.startGame(
                    intent.getIntExtra(NIVEL, 0),
                    intent.getIntExtra(BOARD, 0),
                    intent.getIntExtra(PONTOS, 0),
                    intent.getLongExtra(TEMPO, -1)
                )
            } else
                singlePlayer.startGame()
        }

        binding.boardGame.registerListener(this)
        binding.imageView.setImageBitmap(CurrentUser.imgdata)
        binding.boardGame.updateBoard(singlePlayer.returnboardcells())

        singlePlayer.cellsLiveData.observe(this) { updateCells(it) }
        singlePlayer.fimLiveData.observe(this) { updateFim(it) }
        singlePlayer.pontosLiveData.observe(this) { updatePontos(it) }
        singlePlayer.timerCount.observe(this){updateTimer(it)}
        singlePlayer.nextLevel.observe(this){updateLevel(it)}
    }

    private fun updateLevel(estado : Boolean){
        binding.boardGame.isVisible = false
        binding.NextLevelTimer.isVisible = true
        binding.timertitle.isVisible = true
        binding.timer.isVisible = false

        object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.NextLevelTimer.text= ((millisUntilFinished/1000) + 1).toString()
                }
                override fun onFinish() {
                    binding.boardGame.isVisible = true
                    binding.NextLevelTimer.isVisible = false
                    binding.timer.isVisible = true
                    binding.timertitle.isVisible = false
                    singlePlayer.startTimer(singlePlayer.timerCount.value!!)
                }
            }.start()
    }

    private fun updateFim(estado : Boolean){
        binding.boardGame.isVisible = false
        binding.NextLevelTimer.isVisible = false
        binding.timertitle.isVisible = false
        binding.timer.isVisible = false
        binding.btnMenuInicial.isVisible = true
        binding.tryAgain.isVisible = true

        FirebaseDb.addScore(
            PlayerScore(FirebaseAuth.getInstance().currentUser!!.uid, CurrentUser.username, singlePlayer.pontos, singlePlayer.timerCount.value!!.toLong())
        )

        binding.tryAgain.setOnClickListener(){
            finish()
            startActivity(intent)
        }

        binding.btnMenuInicial.setOnClickListener(){
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateValores(valores: Pair<Double, Double>){
        binding.InfoPontos.text = "Linha: " + (valores.first).toString()+ " Coluna: " +(valores.second).toString()
    }

    private fun updateCells(cells: Board) = cells.let {
        binding.boardGame.updateBoard(cells)
    }

    private fun updateTimer(timer : Long){
        binding.timer.text = "$timer"
    }

    private fun updatePontos(pontos : Int){
        binding.InfoPontos.text = String.format(getString(R.string.Pontos),pontos)
    }

    override fun onCellTouched(row: Int, col: Int) {
        singlePlayer.updateSelectedCell(row, col)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta de Jogo")
        builder.setMessage("Se saires agora, o jogo nao será salvo!")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        builder.show()
    }
}


