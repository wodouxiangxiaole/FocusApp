package com.xd.focusapp.ui.collection.Game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat.getColor
import com.xd.focusapp.R

class Player(context: Context, positionX:Double, positionY:Double, radius:Double) {

    private var positionX:Double
    private var positionY:Double
    private var radius:Double
    private var paint: Paint

    init {
        this.positionX = positionX
        this.positionY = positionY
        this.radius = radius

        paint = Paint()

        val color = getColor(context, R.color.red)
        paint.color = color

    }

    fun draw(canvas: Canvas) {
        // println("debug: draw circle called()")

        canvas.drawCircle(positionX.toFloat(), positionY.toFloat(), radius.toFloat(), paint)

    }

    fun update() {

    }
}