package com.xd.focusapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xd.focusapp.databinding.ActivityMainBinding
import com.xd.focusapp.ui.collection.CollectionViewModel
import com.xd.focusapp.ui.collection.Game.LaunchGame

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


}