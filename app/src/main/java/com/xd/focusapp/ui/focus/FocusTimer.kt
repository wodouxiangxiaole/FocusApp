package com.xd.focusapp.ui.focus

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.*
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.xd.focusapp.R
import java.time.Instant


class FocusTimer: AppCompatActivity() {
    /** Messenger for communicating with the service.  */
    private var mService: FocusService? = null

    /** Flag indicating whether we have called bind on the service.  */
    private var bound: Boolean = false
    private lateinit var countDown: TextView
    private lateinit var appContext: Application
    lateinit var timerLiveData: MutableLiveData<String>
    private var that: LifecycleOwner = this

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFY_ID = 0
    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mService = (service as FocusService.LocalBinder).getService()
            bound = true

            timerLiveData = mService?.timerCountDownLiveData!!
            timerLiveData?.observe(that, { gg->
                println("debuggg livedata observe " + gg)
                countDown.text = gg
            })
        }

        override fun onServiceDisconnected(className: ComponentName) {
            /** This is called when the connection with the service has been
             *  unexpectedly disconnected -- that is, its process crashed.
             */
            mService = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_timer)
//        viewModel = ViewModelProvider(this).get(FocusViewModel::class.java)
        countDown = findViewById(R.id.countDown)

        if (bound != true) {
            var timeInMillSec = intent.getIntExtra("time",0)
            val intent = Intent (this, FocusService::class.java)
            intent.putExtra("timeInMillSec",timeInMillSec)
            startService(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("serviceConnected", bound)

    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, FocusService::class.java), mConnection,
            BIND_AUTO_CREATE
        )
        // Bind to LocalService
        Intent(this, FocusService::class.java).also { intent ->
            appContext = this.getApplicationContext() as Application
            appContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (bound == true) {
            countDown.text = mService?.timeDisplay
        }
    }
    override fun onStop() {
        super.onStop()
        if (bound == true) {
//            unbindService(mConnection)
            bound = false
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (mService?.isInFocus == true) {
            createNotifyChannel()

            val intent = Intent(this, FocusTimer::class.java)
            val pendingIntent = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notify = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Focus App")
                .setContentText("Go back immediately to save your plant")
                .setSmallIcon(R.drawable.flower_drawing)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()

            val notifyManger = NotificationManagerCompat.from(this)
            notifyManger.notify(NOTIFY_ID, notify)

            // message service to take a note on the time
            mService?.killPlantTimer = Instant.now().epochSecond

            finish()
        }
    }

    private fun createNotifyChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
