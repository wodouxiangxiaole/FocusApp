package com.xd.focusapp

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.xd.focusapp.databinding.ActivityMainBinding
import com.xd.focusapp.ui.collection.CollectionViewModel
import com.xd.focusapp.ui.collection.Game.LaunchGame

class MainActivity : AppCompatActivity() {
    private lateinit var db:Database
    private lateinit var user:MutableMap<String,String>

    private lateinit var binding: ActivityMainBinding

    lateinit var auth: FirebaseAuth

    private lateinit var menu:Menu

    private lateinit var sp:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




//        val email = intent.getStringExtra("email")
//        val name = intent.getStringExtra("name")
//        val id = intent.getStringExtra("id")
//        val idToken = intent.getStringExtra("idToken")

        sp = getSharedPreferences("userSp", Context.MODE_PRIVATE)


        //***************************************************************//
        //***************************************************************//
        var uemail = intent.getStringExtra("email")

        // if email == null ==> check the sharedPreference
        // check if user already login
        if(uemail == null){
            uemail = sp.getString("email", null)
        }

        if(uemail != null){
            auth = FirebaseAuth.getInstance()
            db = Database()

            val query = "select * from users where email = '${uemail}';"
            //***************************************************************//
            //***************************************************************//

            // for test purpose
            //val uemail = intent.getStringExtra("uemail")
            user = db.getUser(query)

            var editor: SharedPreferences.Editor = sp.edit().apply {
                putString("name", user["user_name"])
                putString("email", user["email"])
                putString("credits", user["credits"])
                putString("uid", user["uid"])
            }
            editor.commit()
        }



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_setting,
                R.id.navigation_activity,
                R.id.navigation_focus,
                R.id.navigation_spinner,
                R.id.navigation_collection
            )
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if(uemail == null){
//            navView.menu.findItem(3).isVisible = false
//
        }
    }

    fun click(view: View){
        val intent = Intent(this, LaunchGame::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        this.menu = menu!!

        updateMenuTitles()

        return true
    }


    fun updateMenuTitles(){
        // change to proper uid later on
        // find menu item and replace item title to current credits

        menu!!.findItem(R.id.credits).title = sp.getString("credits", "0")

        val credits = sp.getString("credits", "0")

        if( intent.getStringExtra("email") != null) {

            db.updateUserCredits(credits!!.toInt())
        }
    }

    override fun onDestroy() {
        super.onDestroy()


    }




}