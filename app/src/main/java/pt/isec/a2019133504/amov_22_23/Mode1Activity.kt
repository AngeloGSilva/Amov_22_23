package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.channels.ticker
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.SinglePlayer
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding
import kotlin.math.sin


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {
    private val singlePlayer = SinglePlayer()
    private lateinit var binding: ActivityMode1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageURI(ProfileActivity.imgdata)

        binding.boardGame.updateCells(singlePlayer.returnboardcells())

        singlePlayer.cellsLiveData.observe(this) { updateCells(it) }
        singlePlayer.fimLiveData.observe(this) { updateFim(it) }
        singlePlayer.pontosLiveData.observe(this) { updatePontos(it) }
        singlePlayer.timerCount.observe(this){updateTimer(it)}
        singlePlayer.nextLevel.observe(this){updateLevel(it)}
    }

    private fun updateLevel(estado : Boolean){

        /*val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta de Jogo")
        builder.setMessage("Jogo em pausa por 5 segundos!")
        builder.setCancelable(false)
        val caixaAlerta = builder.create()
        caixaAlerta.show()
        val handler = Handler()
        handler.postDelayed({
            caixaAlerta.cancel()
            singlePlayer.startTimer(singlePlayer.pause)
        }, 5000)*/

        binding.boardGame.isVisible = false
        binding.NextLevelTimer.isVisible = true
        binding.timertitle.isVisible = true
        binding.timer.isVisible = false

        object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.NextLevelTimer.text= (millisUntilFinished/1000).toString()
                }
                override fun onFinish() {
                    singlePlayer.startTimer(singlePlayer.pause)
                    binding.boardGame.isVisible = true
                    binding.NextLevelTimer.isVisible = false
                    binding.timer.isVisible = true
                    binding.timertitle.isVisible = false
                }
            }.start()

        /*val handler = Handler()
        handler.postDelayed({
            singlePlayer.startTimer(singlePlayer.pause)
            binding.boardGame.isVisible = true;
            binding.timer.isVisible = false;
            binding.timertitle.isVisible = false;
        }, 5000)*/

    }

    private fun updateFim(estado : Boolean){
        //TODO melhorar mensagem de fim e guardar pontos etc
        /*val builder = AlertDialog.Builder(this)
        builder.setTitle("Fim Jogo")
        builder.setMessage(singlePlayer.pontos.toString())
        builder.setCancelable(false)
        val caixaAlerta = builder.create()
        caixaAlerta.show()*/
        binding.boardGame.isVisible = false
        binding.NextLevelTimer.isVisible = false
        binding.timertitle.isVisible = false
        binding.timer.isVisible = false
        binding.btnAddFirestore.isVisible = true
        binding.btnMenuInicial.isVisible = true
        binding.tryAgain.isVisible = true

        binding.btnAddFirestore.setOnClickListener(){
            addDataToFirestore()
        }

        binding.tryAgain.setOnClickListener(){
            finish()
            startActivity(intent)
        }

        binding.btnMenuInicial.setOnClickListener(){
            /*val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)*/
            finish()
        }
    }

    private fun updateValores(valores: Pair<Double, Double>){
        binding.InfoPontos.text = "Linha: " + (valores.first).toString()+ " Coluna: " +(valores.second).toString()
    }

    private fun updateCells(cells: Array<Array<Cell>>?) = cells?.let {
        binding.boardGame.updateCells(cells)
    }

    private fun updateTimer(timer : Long){
        binding.timer.text = "$timer"
    }

    private fun updatePontos(pontos : Int){
        binding.InfoP.text = pontos.toString()
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

    fun addDataToFirestore() {
        val db = Firebase.firestore

        val scores = hashMapOf(
            "Email" to "tttt",
            "Pontuacao" to singlePlayer.pontos,
            "Time" to 69
        )

        db.collection("Top5Scores").document("Top1").set(scores)
            .addOnSuccessListener {
                Log.i(TAG, "addDataToFirestore: Success")
            }
            .addOnFailureListener { e->
                Log.i(TAG, "addDataToFirestore: ${e.message}")
            }
    }
}


