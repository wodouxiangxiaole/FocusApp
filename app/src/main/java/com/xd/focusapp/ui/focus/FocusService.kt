package com.xd.focusapp.ui.focus

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.MutableLiveData


class FocusService : Service() {
    private var timeInMillSec: Int = 0

    var timeDisplay: String = ""
    // Binder given to clients
    private val binder = LocalBinder()
    lateinit var timerCountDownLiveData: MutableLiveData<String>
    var isInFocus: Boolean = false


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("debuggg onStartCommand")
        timeInMillSec = intent?.getIntExtra("timeInMillSec",0)!!
        timerCountDownLiveData = MutableLiveData<String>()
        var timer: CountDownTimer? = null

        if (timer != null) {
            timer?.cancel()
        }

        timer = object: CountDownTimer(timeInMillSec.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var totalTimeInSec = Math.ceil((millisUntilFinished / 1000).toDouble())
                var secDisplay = "00"
                var minDisplay = "00"
                var hourDisplay = "00"
                if (totalTimeInSec >= 60) {
                    secDisplay = ((totalTimeInSec % 60).toInt()).toString()
                    minDisplay = ((totalTimeInSec / 60).toInt()).toString()
                    var timeInMin = totalTimeInSec / 60

                    if (timeInMin >= 60) {
                        minDisplay = ((timeInMin % 60).toInt()).toString()
                        hourDisplay = ((timeInMin / 60).toInt()).toString()
                    }
                } else {
                    secDisplay = (totalTimeInSec.toInt()).toString()
                }

                timeDisplay = String.format("%s:%s:%s", hourDisplay, minDisplay, secDisplay)
                println("debuggg livedata set " + timeDisplay)

                timerCountDownLiveData.value = timeDisplay
            }

            override fun onFinish() {
                timerCountDownLiveData.value = "DONE"
                isInFocus = false
            }


        }
        timer?.start()
        isInFocus = true

        return super.onStartCommand(intent, flags, startId)

    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): FocusService = this@FocusService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}