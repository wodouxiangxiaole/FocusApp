package com.xd.focusapp

import android.content.Intent
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
        //val uemail = intent.getStringExtra("uemail")
        val query = "select * from users where email = ${email};"
        user = db.getUser(query)

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

        val dbViewModel =
            ViewModelProvider(this).get(DatabaseViewModel::class.java)

        // change to proper uid later on
//        dbViewModel.db.getCredits(1)

        // find menu item and replace item title to current credits
//        menu!!.findItem(R.id.credits).title = dbViewModel.db.getCredits(1)


        return true
    }


}