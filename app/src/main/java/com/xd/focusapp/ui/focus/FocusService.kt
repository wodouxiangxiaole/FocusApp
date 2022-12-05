package com.xd.focusapp.ui.focus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.MutableLiveData
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R
import java.time.Instant


class FocusService : Service() {
    private var myService: FocusService? = null
    private var timeInMillSec: Int = 0
    private var randome: Long = Instant.now().epochSecond

    var timeDisplay: String = ""
    // Binder given to clients
    private val binder = LocalBinder()
    lateinit var timerCountDownLiveData: MutableLiveData<String>
    var isInFocus: Boolean = false
    var killPlantTimer: Long = 0
    val CHANNEL_ID = "channelID2"
    val CHANNEL_NAME = "channelName2"
    val NOTIFY_ID = 2
    private lateinit var that: Context

    override fun onCreate() {
        super.onCreate()

        myService = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("debuggg onStartCommand " + randome)
        timeInMillSec = intent?.getIntExtra("timeInMillSec",0)!!
        timerCountDownLiveData = MutableLiveData<String>()
        var timer: CountDownTimer? = null
        that = this

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

                //if (//global variable is set && global variable > 5 sec) then kill tree
                if (killPlantTimer != 0L && Instant.now().epochSecond.minus(killPlantTimer) >= 10) {
                    //kill your damn tree
                    println("debuggg time to kill plant, use has left app for " + (Instant.now().epochSecond.minus(killPlantTimer)))
                    createNotifyChannel()

                    val intent = Intent(that, MainActivity::class.java)
                    val pendingIntent = TaskStackBuilder.create(that).run {
                        addNextIntent(intent)
                        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                    }

                    val notify = NotificationCompat.Builder(that, CHANNEL_ID)
                        .setContentTitle("Focus App")
                        .setContentText("Your plant has been killed")
                        .setSmallIcon(R.drawable.flower_drawing)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .build()

                    val notifyManger = NotificationManagerCompat.from(that)
                    notifyManger.notify(NOTIFY_ID, notify)
                    notifyManger.deleteNotificationChannel("channelID");
                    timer?.cancel()
                    (that as FocusService).stopSelf()

                }
                timeDisplay = String.format("%s:%s:%s", hourDisplay, minDisplay, secDisplay)
                println("debuggg livedata set " + timeDisplay + " "+ randome)

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

    //function to receive the message and write it to a global variable
    //
    private fun createNotifyChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
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

        myService = null
    }

}