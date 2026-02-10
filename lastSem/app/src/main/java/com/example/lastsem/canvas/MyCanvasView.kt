package com.example.lastsem.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyCanvasView : View {

    private val paint = Paint().apply { isAntiAlias = true }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)

        val cx = width / 2f

        val rectW = 450f
        val rectH = 750f

        val left = cx - rectW / 2
        val top = 120f
        val right = cx + rectW / 2
        val bottom = top + rectH

        paint.color = Color.DKGRAY
        paint.style = Paint.Style.FILL
        canvas.drawRect(left, top, right, bottom, paint)

        val radius = 110f

        val redY = 260f
        val orangeY = 495f
        val greenY = 730f

        paint.textSize= 60f
        paint.strokeWidth = 675f
        paint.isFakeBoldText = true
        paint.color = Color.BLACK
        canvas.drawText("Traffic Signals", cx - 200f, 100f, paint)
        paint.color = Color.RED
        canvas.drawCircle(cx, redY, radius, paint)

        paint.color = Color.WHITE
        paint.textSize = 45f
        canvas.drawText("STOP", cx - 70f, redY + 15f, paint)

        paint.color = Color.YELLOW
        canvas.drawCircle(cx, orangeY, radius, paint)

        paint.color = Color.BLACK
        paint.textSize = 45f
        canvas.drawText("WAIT", cx - 70f, orangeY + 15f, paint)

        paint.color = Color.GREEN
        canvas.drawCircle(cx, greenY, radius, paint)

        paint.color = Color.BLACK
        paint.textSize = 45f
        canvas.drawText("GO", cx - 30f, greenY + 15f, paint)

        paint.color = Color.BLACK
        paint.strokeWidth = 55f
        canvas.drawLine(cx, bottom, cx, bottom + 320f, paint)
    }
}
