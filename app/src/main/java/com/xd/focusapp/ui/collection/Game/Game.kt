package com.xd.focusapp.ui.collection.Game

import android.content.Context
import android.graphics.Canvas
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.androidstudio2dgamedevelopment.GameLoop

class Game(context: Context): SurfaceView(context), SurfaceHolder.Callback{
    private var joystick: Joystick ?= null
    private var player: Player ?= null
    private var thread: GameLoop
    private lateinit var surfaceHolder: SurfaceHolder

    init {
        // add callback
        holder.addCallback(this)

        player = Player(context, 2*500.0, 500.0, 80.0)
        joystick = Joystick(275, 700, 70, 30)

        thread = GameLoop(this, holder)

        println("debug: Game() called")

    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        println("debug: surfaceCreated() called")

        if (thread.state.equals(Thread.State.TERMINATED)) {
            val surfaceHolder = holder
            surfaceHolder.addCallback(this)
            thread = GameLoop(this, surfaceHolder)
        }

        surfaceHolder = p0


        draw()
//
        thread.startLoop()
        thread.run()


    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        println("debug: surfaceCreated() called")
//        var retry = true
//        while(retry){
//            try{
//                thread.startLoop()
//                thread.join()
//            }
//            catch (e: Exception){
//                e.printStackTrace()
//            }
//
//            retry = false
//        }
    }



    fun update(){
        player!!.update()
        joystick!!.update()
    }

    private fun draw(){
        val c = surfaceHolder.lockCanvas()
        player!!.draw(c)
        joystick!!.draw(c)
        holder.unlockCanvasAndPost(c)
    }

//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//
//    }

//    override fun draw(canvas: Canvas?) {
//        super.draw(canvas)
//
//        // Draw game objects
//        player!!.draw(canvas!!)
//
//        // Draw game panels
//        joystick!!.draw(canvas!!)
//
//
//    }


}