package pt.isec.a2019133504.amov_22_23.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import pt.isec.a2019133504.amov_22_23.Data.Board
import kotlin.math.abs


class BoardView (context: Context, attributeSet: AttributeSet) : View(context, attributeSet),
    GestureDetector.OnGestureListener{

    private lateinit var board : Board

    // these are set in onDraw
    private var cellSizePixels = 0F

    private var listener: OnTouchListener? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thickLinePaintForSelect = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#9c4239")
        strokeWidth = 10F
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#ffdad5")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.textSize = 60f
        cellSizePixels = width / Board.sz.toFloat()
        //updateMeasurements(width)
        drawLines(canvas)
        drawText(canvas)
    }


    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * cellSizePixels + 4F,
            r * cellSizePixels + 4F,
            (c + 1) * cellSizePixels - 4F,
            (r + 1) * cellSizePixels - 4F,
            paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        //TODO MELHORAR FUNCAO DE DESENHO
        //Desenha Linhas Verticais
        for (i in 0 until Board.sz) {
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                (cellSizePixels * Board.sz), // height.toFloatheight.toFloat()
                thickLinePaint
            )

        }
        //Desenha Linhas horizontais
        for (i in 0 until Board.sz+1) {
            canvas.drawLine(
                0F,
                i * cellSizePixels,
                (cellSizePixels * Board.sz), //width.toFloat()
                i * cellSizePixels,
                thickLinePaint
            )
        }
    }

    private fun drawText(canvas: Canvas) {
        textPaint.textSize = 60f
        for (r in 0 until Board.sz) {
            for (c in 0 until Board.sz) {
                if (!(r%2==0 && c%2==0) && !(r==c || (r%2!=0 && c%2!=0)))
                    fillCell(canvas, r, c, startingCellPaint)

                try {
                    canvas.drawText(
                        (board.cells[r][c].toDouble().toInt().toString()),
                        (c + 0.5f) * cellSizePixels,
                        (r + 0.5f) * cellSizePixels,
                        textPaint
                    )
                } catch (_ : Exception) {
                    canvas.drawText(
                        board.cells[r][c],
                        (c + 0.5f) * cellSizePixels,
                        (r + 0.55f) * cellSizePixels,
                        textPaint
                    )
                }
            }
        }
    }

    val gestureDetector = GestureDetector(context,this)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event))
            return true
        return super.onTouchEvent(event)
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (abs(velocityX)>abs(velocityY)) {
            if (abs(event1.y - event2.y) > cellSizePixels) return true
            val y = (event1.y + event2.y) / 2
            val row = (y / cellSizePixels).toInt()
            //System.out.println(row)
            //selectedRow = row
            if (row % 2 == 0) {
                listener?.onCellTouched(row,-1)

            }
        }else{
            if (abs(event1.x - event2.x) > cellSizePixels) return true
            val x = (event1.x+event2.x)/2
            val col = (x/cellSizePixels).toInt()
            if (col % 2 == 0) {
                listener?.onCellTouched(-1,col)

            }
        }

        invalidate()
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        //System.out.println("onDown:" + p0)
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        //System.out.println("onShowPress:" + p0)
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        //System.out.println("onSingleTapUp:"+p0)
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //System.out.println("onScroll:"+ p0 + p1+ p2+ p3)
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        //System.out.println("onLongPress:")
    }

    fun updateBoard(board:  Board) {
        this.board = board
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}