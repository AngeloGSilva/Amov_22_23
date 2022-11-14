package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import pt.isec.a2019133504.amov_22_23.Data.Board
import pt.isec.a2019133504.amov_22_23.databinding.ActivityCameraBinding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMainBinding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding
import kotlin.coroutines.CoroutineContext

class Mode1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMode1Binding
    private lateinit var boardView: BoardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_mode1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }







    private fun GestureDetector(context: CoroutineContext, mode1Activity: Mode1Activity): Any {
        TODO("Not yet implemented")
    }


}


