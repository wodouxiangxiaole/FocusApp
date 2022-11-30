package com.xd.focusapp.ui.collection.Game

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameLoop(game:Game, surfaceHolder: SurfaceHolder):Thread() {
    private var isRunning:Boolean = false
    private var surfaceHolder: SurfaceHolder
    private var game:Game
    private val targetFPS = 50


    init{
        this.surfaceHolder = surfaceHolder
        this.game = game
    }

    fun startLoop(){
        isRunning = true
        start()
    }

    fun endLoop(){
        isRunning = false
    }

    override fun run(){
        super.run()

        var canvas:Canvas
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        while(isRunning){
            startTime = System.nanoTime()
            try {
                canvas = surfaceHolder.lockCanvas()

                // use synchronize method to lock the canvas
                // critical section
                synchronized(surfaceHolder){
                    game.update()
                    game.draw(canvas)
                }

                // unlock canvas
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
            catch (e:Exception){
                e.printStackTrace()
            }

            // get time slot
            timeMillis = (System.nanoTime() - startTime) / 1000000

            // we need to wait this time and then refresh the canvas
            waitTime = targetTime - timeMillis

            // println("time: ${timeMillis}")

            try{
                if(waitTime > 0){
                    sleep(waitTime)
                }

            }
            catch (e: Exception){
                e.printStackTrace()
            }


        }

    }

}