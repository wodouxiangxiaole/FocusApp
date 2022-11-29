package com.xd.focusapp.ui.collection.Game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Joystick(centerPositionX: Int, centerPositionY: Int, outerCircleRadius: Int, innerCircleRadius:Int) {
    private var innerCirclePaint: Paint
    private val outerCirclePaint: Paint
    private var outerCircleRadius: Int
    private var innerCircleRadius: Int
    private var outerCircleCenterPositionX: Int
    private var outerCircleCenterPositionY: Int
    private var innerCircleCenterPositionX: Int
    private var innerCircleCenterPositionY: Int



    init {
        outerCircleCenterPositionX = centerPositionX
        outerCircleCenterPositionY = centerPositionY
        innerCircleCenterPositionX = centerPositionX
        innerCircleCenterPositionY = centerPositionY
        this.outerCircleRadius = outerCircleRadius
        this.innerCircleRadius = innerCircleRadius

        // paint of circles
        outerCirclePaint = Paint()
        outerCirclePaint.color = Color.GRAY
        outerCirclePaint.style = Paint.Style.FILL_AND_STROKE

        innerCirclePaint = Paint()
        innerCirclePaint.color = Color.BLUE
        innerCirclePaint.style = Paint.Style.FILL_AND_STROKE
    }

    fun draw(canvas: Canvas) {
        // draw the circle
        canvas.drawCircle(outerCircleCenterPositionX.toFloat(),
            outerCircleCenterPositionY.toFloat(),
            outerCircleRadius.toFloat(),
            outerCirclePaint)

        canvas.drawCircle(innerCircleCenterPositionX.toFloat(),
            innerCircleCenterPositionY.toFloat(),
            innerCircleRadius.toFloat(),
            innerCirclePaint)
    }

    fun update() {

    }

}
