package com.xd.focusapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xd.focusapp.R
import com.xd.focusapp.databinding.ActivityLoginBinding
import com.xd.focusapp.databinding.ActivitySignupBinding

class ActivitySignup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toLogin = binding.toLoginTv

        toLogin.setOnClickListener{ startActivity(Intent(this, ActivityLogin::class.java)) }
    }
}