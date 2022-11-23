package pt.isec.a2019133504.amov_22_23

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.MyViewModel
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(),BoardView.OnTouchListener {

    private val viewModel : MyViewModel by viewModels()

    private lateinit var binding: ActivityMode1Binding
    //private var cells =viewModel.mathGame.getContent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        //binding.boardGame.updateCells(cells)
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

    }

/*    override fun onStart() {
        binding.boardGame.updateCells(viewModel.mathGame.board.cells)
        super.onStart()
    }*/

    private fun updateVitoria(estado : Boolean){
        if (estado)
            Toast.makeText(baseContext, "Acertou", Toast.LENGTH_LONG).show()
    }

    private fun updateValores(valores: Pair<Int, Int>){
        binding.InfoPontos.text = (valores.first).toString()+(valores.second).toString()
    }

    private fun updateCells(cells: Array<Array<Cell>>?) = cells?.let {
        binding.boardGame.updateCells(cells)
    }

    private fun updatePontos(pontos : Int){
        binding.InfoPontos.text = pontos.toString()
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


