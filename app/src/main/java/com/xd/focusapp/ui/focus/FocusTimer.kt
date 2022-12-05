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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R
import com.xd.focusapp.ui.collection.CollectionViewModel
import com.xd.focusapp.ui.collection.CollectionViewModelFactory
import com.xd.focusapp.ui.spinner.SpinnerFinishDialog
import java.time.Instant


class FocusTimer: AppCompatActivity() {
    /** Messenger for communicating with the service.  */
    private var mService: FocusService? = null
    private var randome: Long = Instant.now().epochSecond
    private lateinit var db: Database

    /** Flag indicating whether we have called bind on the service.  */
    private var bound: Boolean = false
    private lateinit var countDown: TextView
    private lateinit var appContext: Application
    lateinit var timerLiveData: MutableLiveData<String>
    private var that: LifecycleOwner = this
    private var timeInMillSec: Int = 0
    private val sectors = arrayOf("common", "uncommon", "rare", "legendary")
    private lateinit var collectionViewModel: CollectionViewModel
    private var hasDone: Boolean = false
    private lateinit var uemail: String
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
            timerLiveData?.observe(that, { it->
                println("debuggg livedata observe " + it + " " + randome)
                if (it == "DONE" ) {
                    if (!hasDone) {
                        // give credit and plant
                        timerCompleted()
                        hasDone = true
                    }
                } else {
                    countDown.text = it
                }
            })

            /**
             * If user resume the app within 15 seconds, the killPlantTimer will be reset
             */
            println("debuggg reset kill plan timer to 0")
            mService?.killPlantTimer = 0L
        }

        override fun onServiceDisconnected(className: ComponentName) {
            /** This is called when the connection with the service has been
             *  unexpectedly disconnected -- that is, its process crashed.
             */
            mService = null
            bound = false
        }
    }

    fun timerCompleted(){
        val sp = this.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        var credits = sp?.getString("credits", "0")!!.toInt()

        val editor = sp?.edit()
        editor?.putString("credits", (credits+timeInMillSec/1000/60).toString())
        editor?.commit()

        //display tree diaglog with a button to main activity focus fragments
        timerFinishPrize()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_timer)
//        viewModel = ViewModelProvider(this).get(FocusViewModel::class.java)
        countDown = findViewById(R.id.countDown)
        if (savedInstanceState != null) {
            bound = savedInstanceState.getBoolean("serviceConnected", false)
        }

        if (bound != true) {
            timeInMillSec = intent.getIntExtra("time",0)
            if (timeInMillSec != 0) {
                val intent = Intent (this, FocusService::class.java)
                intent.putExtra("timeInMillSec",timeInMillSec)
                startService(intent)
            }
        }

        val sp = getSharedPreferences("userSp", Context.MODE_PRIVATE)
        uemail = sp!!.getString("email", "").toString()
        val viewModelFactory = CollectionViewModelFactory(uemail)
        collectionViewModel =
            ViewModelProvider(this, viewModelFactory).get(CollectionViewModel::class.java)
        db = Database()
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
    override fun onPause() {
        super.onPause()

//        mService?.killPlantTimer = 0L

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

//    override fun onRestart() {
//        super.onRestart()
//        mService?.killPlantTimer = 0L
//    }

    private fun createNotifyChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun timerFinishPrize () {
        val timeToGetPrize = timeInMillSec/1000/60
        if (timeToGetPrize >= 0) {
            var res = 3

            if (timeToGetPrize >= 15 && timeToGetPrize < 30) {
                /** reward common plant */
                res = 3
            } else if (timeToGetPrize >= 30 && timeToGetPrize < 45) {
                /** reward uncommon plant */
                res = 2
            } else if (timeToGetPrize >= 45) {
                res = 1
                /** reward rare plant */
            }
            Toast.makeText(this,"You got ${sectors[3-res]}", Toast.LENGTH_SHORT).show()

            val plant = collectionViewModel.unlock(res,0)

            val dialog = SpinnerFinishDialog()
            val bundle = Bundle()
            bundle.putInt("image", plant.image!!)

            if(plant.toPoints){
                bundle.putInt("key", SpinnerFinishDialog.REPEAT_KEY)
                when(res){
                    3 ->{
                        bundle.putInt("points", SpinnerFinishDialog.COMMON)
                    }
                    2 -> {
                        bundle.putInt("points", SpinnerFinishDialog.UNCOMMON)
                    }
                    1 -> {
                        bundle.putInt("points", SpinnerFinishDialog.RARE)
                    }
                }
            }
            else{
                bundle.putInt("key", SpinnerFinishDialog.NORMAL_KEY)
            }

            dialog.arguments = bundle

            dialog.show(supportFragmentManager,"dialog")
        }
    }

    fun updateDB(){
        val intent = Intent(this, MainActivity::class.java)

        //only update db when user is logon
        if (uemail.isNotEmpty()) {
            val sp = this.getSharedPreferences("userSp", Context.MODE_PRIVATE)
            var credits = sp?.getString("credits", "0")!!.toInt()
            val query = "select * from users where email = '${uemail}';"
            // set user id in db
            db.getUser(query)

            db.updateUserCredits(credits!!)

            intent.putExtra("email",uemail)
        }

        startActivity(intent)
    }
}
