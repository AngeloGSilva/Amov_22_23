package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.ktx.Firebase
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.Data.SinglePlayer
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {
    private val singlePlayer = SinglePlayer()
    private lateinit var binding: ActivityMode1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageBitmap(ProfileActivity.imgdata)

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
        binding.boardGame.isVisible = false
        binding.NextLevelTimer.isVisible = false
        binding.timertitle.isVisible = false
        binding.timer.isVisible = false
        binding.btnAddFirestore.isVisible = true
        binding.btnMenuInicial.isVisible = true
        binding.tryAgain.isVisible = true

        binding.btnAddFirestore.setOnClickListener(){
            //TODO binding de status Message
            if(addDataToFirestore()) {
                //TODO binding de status Message
                binding.btnAddFirestore.isEnabled = false
            }else{
                //TODO binding de status Message
            }
        }

        binding.tryAgain.setOnClickListener(){
            finish()
            startActivity(intent)
        }

        binding.btnMenuInicial.setOnClickListener(){
            finish()
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

    fun addDataToFirestore() : Boolean{
        val db = Firebase.firestore
        var email = SignInActivity.emailUser
        val scores = hashMapOf(
            "Email" to email,
            "Pontuacao" to singlePlayer.pontos,
            "Time" to singlePlayer.timerCount
        )
        var TopScore : Boolean = false
        db.collection("Top5Scores").orderBy("Pontuacao", Direction.ASCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["Pontuacao"].toString().toInt() < singlePlayer.pontos) {
                        db.collection("Top5Scores").document(document.id).set(scores)
                        TopScore = true
                    }
                }
            }
            .addOnFailureListener{result->
                //TODO set e limite 5
            }
        return TopScore
    }


}


