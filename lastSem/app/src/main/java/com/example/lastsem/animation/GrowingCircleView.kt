package com.example.lastsem.animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GrowingCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt() // black color
    }

    private var radius = 50f

    init {
        startGrowingAnimation()
    }

    private fun startGrowingAnimation() {
        val animator = ValueAnimator.ofFloat(50f, 250f)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE

        animator.addUpdateListener {
            radius = it.animatedValue as Float
            invalidate() // redraw
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        canvas.drawCircle(cx, cy, radius, paint)
    }
}
