package com.xd.focusapp.ui.collection.Game

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class Game(context: Context): SurfaceView(context), SurfaceHolder.Callback{
    private val thread:GameLoop
    private lateinit var player:Player

    init {

        // add callback

        holder.addCallback(this)
        thread = GameLoop(this, holder)
        isFocusable = true

        println("debug: Game() called")

    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        println("debug: surfaceCreated() called")
        player = Player(context, 500.0, 500.0, 30.0)
        thread.startLoop()
        thread.run()

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        println("debug: surfaceCreated() called")
        var retry = true
        while(retry){
            try{
                thread.endLoop()
                thread.join()
            }
            catch (e: Exception){
                e.printStackTrace()
            }

            retry = false
        }
    }

    fun update(){
        player.update()
    }

    override fun draw(canvas:Canvas){
        super.draw(canvas)
        player.draw(canvas)

    }


}