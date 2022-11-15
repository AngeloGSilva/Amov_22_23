package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import pt.isec.a2019133504.amov_22_23.Data.Board
import kotlin.math.abs

class BoardView (context: Context, attributeSet: AttributeSet) : View(context, attributeSet),GestureDetector.OnGestureListener {
    //private var sqrtSize = 3
    private var size = 5

    private var cellSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

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
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        numbersOperators(canvas)
    }

    private fun numbersOperators(canvas: Canvas){
        textPaint.textSize = 24F
            for (r in 0 .. size) {
                for (c in 0..size) {
                    if (r % 2 == 0 && c % 2 == 0)
                        canvas?.drawText("12",(c+0.5F) * cellSizePixels,r + cellSizePixels*3/5,textPaint)
                    else
                        canvas?.drawText("X",(c+0.5F) * cellSizePixels,r + cellSizePixels*3/5,textPaint)
                }
            }
    }

    private fun fillCells(canvas: Canvas) {
            if (selectedRow == -1 && selectedCol == -1) return
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
        System.out.println("onLongPress:"+ p0)
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
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
        invalidate()
        return true
    }
}