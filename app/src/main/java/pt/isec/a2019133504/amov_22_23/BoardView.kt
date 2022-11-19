package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.graphics.*
import android.nfc.Tag
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text
import pt.isec.a2019133504.amov_22_23.Data.Board
import java.nio.file.attribute.AclEntry.Builder
import java.text.DecimalFormat
import kotlin.math.abs

class BoardView (context: Context, attributeSet: AttributeSet) : View(context, attributeSet),GestureDetector.OnGestureListener {

    private var size = 5
    private var cellSizePixels = 0F
    private var initialValCelX = 0.5f
    private var initialValCelY = 0.55f
    private var numbertextsize = 60f
    private var nivel = 1
    var board = Board(nivel)
    var text = findViewById<TextView>(R.id.textView2)
    private var selectedRow = -1
    private var selectedCol = -1
    private var longPressRow = -1
    private var longPressCol = -1

    var CORRETA = false
        get() = field

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    private val suggestedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#D6C9C6")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        if (CORRETA) {
            Toast.makeText(context, "Acertaste na expressão correta!", Toast.LENGTH_SHORT).show()
            //Log.i(VIEW_LOG_TAG,"Acertaste na expressão correta!")
            CORRETA=false
            nivel+=1
            board = Board(nivel)
            fillCells(canvas)
            drawLines(canvas)
            updateText()
            numbersOperators(canvas)
        }else {
            cellSizePixels = (width / size).toFloat()
            fillCellsSuggested(canvas)
            fillCells(canvas)
            drawLines(canvas)
            numbersOperators(canvas)
            updateText()
        }
        longPressRow = -1
        longPressCol = -1
        selectedCol = -1
        selectedRow = -1
    }

     private fun numbersOperators(canvas: Canvas){
        textPaint.textSize = numbertextsize
            for (r in 0 .. size-1) {
                for (c in 0..size-1) {
                    if (r % 2 == 0 && c % 2 == 0) {
                        canvas?.drawText(
                            DecimalFormat("#").format(board.board[r][c]).toString(),
                            (c + initialValCelX) * cellSizePixels,
                            (r + initialValCelY) * cellSizePixels,
                            textPaint
                        )
                    }else if((r==c && (r%2!=0 || c%2!=0)) || (r%2!=0 && c%2!=0)){
                        continue
                    }else
                        canvas?.drawText(board.board[r][c].toString(),(c+initialValCelX) * cellSizePixels,(r+initialValCelY) * cellSizePixels,textPaint)
                }
            }

        /*for (r in 0 until size) {
            for (c in 0 until size) {
                canvas.drawText(board.board[r][c],(c+0.5F) * cellSizePixels,r + cellSizePixels*3/5,textPaint)
            }
        }*/
    }

    private fun fillCellsSuggested(canvas: Canvas) {
        //TODO MELHORAR FUNCAO...(SIZE-1) PARA NAO PINTAR UN QUADRADO A MAIS
        for (r in 0 .. size-1) {
            for (c in 0..size-1) {
                if (c == longPressCol)
                    fillCell(canvas, r, c, suggestedCellPaint)
                if (r == longPressRow)
                    fillCell(canvas, r, c, suggestedCellPaint)
            }
        }
    }

    private fun fillCells(canvas: Canvas) {
        //TODO MELHORAR FUNCAO...(SIZE-1) PARA NAO PINTAR UN QUADRADO A MAIS
            for (r in 0 .. size-1) {
                for (c in 0..size-1) {
                    if (c == selectedCol && selectedRow == -1)
                        fillCell(canvas, r, c, selectedCellPaint)
                    if (r == selectedRow && selectedCol == -1)
                        fillCell(canvas, r, c, selectedCellPaint)
                }
            }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c + 1) * cellSizePixels, (r + 1) * cellSizePixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        //TODO MELHORAR FUNCAO DE DESENHO
        //Desenha Linhas Verticais
        for (i in 0 until size) {
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                (cellSizePixels * size), // height.toFloatheight.toFloat()
                thickLinePaint
            )

        }

        //Desenha Linhas horizontais
        for (i in 0 until size+1) {
            canvas.drawLine(
                0F,
                i * cellSizePixels,
                (cellSizePixels * size), //width.toFloat()
                i * cellSizePixels,
                thickLinePaint
            )
        }
    }

    fun checkVitoria(){
        if (selectedRow != -1 && (board.getResultadoLinha(selectedRow) == board.maiorValor || board.getResultadoLinha(selectedRow) == board.SecondMaiorValor))
            CORRETA = true
        else if (selectedCol != -1 && (board.getResultadoColuna(selectedCol) == board.maiorValor || board.getResultadoColuna(selectedCol) == board.SecondMaiorValor))
            CORRETA = true
    }

/*    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        selectedRow = (y / cellSizePixels).toInt()
        selectedCol = (x / cellSizePixels).toInt()
        invalidate()
    }*/

    val gestureDetector = GestureDetector(context,this)


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event))
            return true
        if(!gestureDetector.isLongpressEnabled()){
            invalidate()
        }
        return super.onTouchEvent(event)
    }


    override fun onDown(p0: MotionEvent?): Boolean {
        System.out.println("onDown:" + p0)
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        System.out.println("onShowPress:" + p0)
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        System.out.println("onSingleTapUp:"+p0)
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        System.out.println("onScroll:"+ p0 + p1+ p2+ p3)
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        val row = (p0!!.y/cellSizePixels).toInt()
        val col = (p0!!.x/cellSizePixels).toInt()
        if (row % 2 == 0)
            longPressRow = row
        if (col % 2 == 0)
            longPressCol = col
        System.out.println("onLongPress:"+ row+col)
        invalidate()
    }

    fun updateText(){
        board.attresults()
        board.setMaiorValor()
        board.setSecondMaiorValor()
       /* val myTextView = findViewById<TextView>(R.id.textView2)
        myTextView.text = board.linhasValores.toString()*/
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
/*        var buff = board?.getallResult().toString()
        val myTextView = findViewById<TextView>(R.id.textView2)
        myTextView.text = buff*/
        if (abs(velocityX)> abs(velocityY)) {
            val y = (event1.y + event2.y) / 2
            val row = (y / cellSizePixels).toInt()
            System.out.println(row)
            //selectedRow = row
            if (row % 2 == 0) {
                selectedRow = row
                selectedCol = -1
                System.out.println("Linha" + row + "e Coluna " + selectedCol)
            }
        }else{
            val x = (event1.x+event2.x)/2
            val col = (x/cellSizePixels).toInt()
            if (col % 2 == 0) {
                selectedCol = col
                selectedRow = -1
                System.out.println("Coluna" + col + "Linha "+ selectedRow)
            }
        }
/*        Toast.makeText(context, "Fling Gesture", Toast.LENGTH_LONG).show()
        System.out.println("onFling: " +event1 + event2)
        Log.d(VIEW_LOG_TAG, "onFling: $event1 $event2")*/
        checkVitoria()
        invalidate()
        return true
    }
}