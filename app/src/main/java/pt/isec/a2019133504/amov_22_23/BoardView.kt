package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import pt.isec.a2019133504.amov_22_23.Data.Cell
import kotlin.math.abs


class BoardView (context: Context, attributeSet: AttributeSet) : View(context, attributeSet),
    GestureDetector.OnGestureListener{

    private var size = 5

    // these are set in onDraw
    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0


    private var listener: BoardView.OnTouchListener? = null

    private var cells : Array<Array<Cell>>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }


    private val thickLinePaintForSelect = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 10F
    }


    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }


    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#DAF7A6")
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.textSize = 60f
        cellSizePixels = width / size.toFloat()
        //updateMeasurements(width)
        drawLines(canvas)
        drawText(canvas)
        fillCells(canvas)
    }


    private fun fillCells(canvas: Canvas) {
        //TODO MELHORAR FUNCAO...(SIZE-1) PARA NAO PINTAR UN QUADRADO A MAIS
        for (r in 0 until  size) {
            for (c in 0 until size) {
                if (c == selectedCol && selectedRow == -1)
                    fillCell(canvas, r, c, thickLinePaintForSelect)
                if (r == selectedRow && selectedCol == -1)
                    fillCell(canvas, r, c, thickLinePaintForSelect)
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


    private fun drawText(canvas: Canvas) {
        textPaint.textSize = 60f
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (r % 2 == 0 && c % 2 == 0) {
                    canvas?.drawText(
                        (cells!![r][c].value.toDouble().toInt().toString()),//TODO MELHORAR ESTA LINHA
                        (c + 0.5f) * cellSizePixels,
                        (r + 0.5f) * cellSizePixels,
                        textPaint
                    )
                }else if((r==c && (r%2!=0 || c%2!=0)) || (r%2!=0 && c%2!=0)){
                    continue
                }else {
                    fillCell(canvas,r,c,startingCellPaint)
                    canvas?.drawText(
                        cells!![r][c].value,
                        (c + 0.5f) * cellSizePixels,
                        (r + 0.55f) * cellSizePixels,
                        textPaint
                    )
                }
            }
        }

        /*cells?.forEach {rows ->
            row.forEach{ cole ->
                val value = cole.value

                val row = rows
                val col = cole
                val valueString = col.value

                val paintToUse = if (col.isOperator) startingCellTextPaint else textPaint
                val textBounds = Rect()
                paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = paintToUse.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(
                    valueString, (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                    (row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2, paintToUse
                )
            }}*/
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
        listener?.onCellTouched(selectedRow,selectedCol)
        invalidate()
        return true
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
        System.out.println("onLongPress:")

    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }


    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }


    fun updateCells(cells:  Array<Array<Cell>>) {
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: BoardView.OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}