package com.xd.focusapp

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")
        val idToken = intent.getStringExtra("idToken")

        db = Database()
        //***************************************************************//
        //***************************************************************//
//        val uemail = intent.getStringExtra("uemail")
//        val query = "select * from users where email = ${uemail};"
        //***************************************************************//
        //***************************************************************//

        // for test purpose
        val query = "select * from users where uid = 1;"
        //val uemail = intent.getStringExtra("uemail")
        user = db.getUser(query)

        val sp = getSharedPreferences("userSp", Context.MODE_PRIVATE)

        var editor: SharedPreferences.Editor = sp.edit().apply {
            putString("name", user["user_name"])
            putString("email", user["email"])
            putString("credits", user["credits"])
            putString("uid", user["uid"])
        }
        editor.commit()

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
        val sp = getSharedPreferences("userSp", Context.MODE_PRIVATE)

        // change to proper uid later on
        // find menu item and replace item title to current credits
        menu!!.findItem(R.id.credits).title = sp.getString("credits", "")

    }




}