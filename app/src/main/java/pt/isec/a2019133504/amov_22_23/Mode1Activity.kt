package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.MathGame
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.View.MyViewModel
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {

    private val viewModel : MyViewModel by viewModels()

    private var paused : Long = 0

    private lateinit var binding: ActivityMode1Binding
    //private var cells =viewModel.mathGame.getContent()
    private lateinit var timerObject : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageURI(ProfileActivity.imgdata)

        //binding.boardGame.updateCells(cells)
        //TODO VERIFICAR SE É A MELHOR MANEIRA DE RESOLVER O CRASH
        binding.boardGame.updateCells(viewModel.mathGame.board.cells)

        viewModel.mathGame.cellsLiveData.observe(this) { updateCells(it) }

        viewModel.mathGame.selectedCellLiveData.observe(this) { updateSelectedCellUI(it) }
        viewModel.mathGame.maioresValores.observe(this) { updateValores(it) }
        viewModel.mathGame.vitoriaLiveData.observe(this) { updateVitoria(it) }
        viewModel.mathGame.pontosLiveData.observe(this) { updatePontos(it) }

/*        viewModel.mathGame.cellsLiveData.observe(this){
            it?.run { updateCells(it) }
        }*/
        //Thread.sleep(10000)
        startTimer(MathGame.level.maxTime)


    }

    private fun startTimer(totalTime : Int){
        // time count down for 30 seconds,
        // with 1 second as countDown interval
        timerObject = object : CountDownTimer((totalTime*1000).toLong(), 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.viewTimer.text = "seconds remaining: " + millisUntilFinished / 1000
                paused = millisUntilFinished / 1000
            }
            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                viewModel.mathGame.resetBoardAtributos()
                binding.viewTimer.text = "Done!"
                binding.boardGame.visibility = View.INVISIBLE
                //startTimer(10)
            }
        }.start()
    }

/*    override fun onStart() {
        binding.boardGame.updateCells(viewModel.mathGame.board.cells)
        super.onStart()
    }*/

    private fun updateVitoria(estado : Boolean){
        if (estado) {
            Toast.makeText(baseContext, "Acertou", Toast.LENGTH_SHORT).show()
            viewModel.mathGame.resetBoardAtributos()
            timerObject.cancel()
            if (paused < MathGame.level.maxTime)
                startTimer(paused.toInt() + MathGame.level.winTime)
            else
                startTimer(MathGame.level.maxTime)
        }
    }

    private fun updateValores(valores: Pair<Int, Int>){
        binding.InfoPontos.text = "Linha: " + (valores.first).toString()+ " Coluna: " +(valores.second).toString()
    }

    private fun updateCells(cells: Array<Array<Cell>>?) = cells?.let {
        binding.boardGame.updateCells(cells)
    }

    private fun updatePontos(pontos : Int){
        binding.InfoP.text = "Pontos: $pontos"
/*        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            binding.ola, scaleX, scaleY)
        animator.start()*/
    }

/*    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            binding.ola.text, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        //animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }*/



    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.boardGame.updateSelectedCellUI(cell.first, cell.second)
/*        val animator = ObjectAnimator.ofFloat(binding.boardGame, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.start()*/
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.mathGame.updateSelectedCell(row, col)
    }
}


