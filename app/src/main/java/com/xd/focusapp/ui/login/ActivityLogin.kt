package com.xd.focusapp.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xd.focusapp.MainActivity
import com.xd.focusapp.databinding.ActivityLoginBinding

import com.xd.focusapp.ui.collection.CollectionViewModel

open class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var loginBtn: Button
    lateinit var loading: ProgressBar
    lateinit var guest: TextView
    lateinit var signUp: TextView
    lateinit var pwReset: TextView
    lateinit var google: Button

    val emailPtn ="[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-zA-Z]+"
    lateinit var progressDialog: ProgressDialog
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//
//        // test
//         val query = "select * from users"
//         db.getAllUser(query)
        // test
//         val query = "select * from users"
//         db.getAllUser(query)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputEmail = binding.inputEmailLogin
        inputPassword = binding.inputPasswordLogin
        loginBtn = binding.loginButton
        loading = binding.loading
        loading.visibility = View.INVISIBLE
        guest = binding.GuestLoginBtn
        signUp = binding.noAccountTv
        //pwReset = binding.forgetPwTv
        google = binding.googleButton
        firebaseAuth = Firebase.auth

        signUp.setOnClickListener{ startActivity(Intent(this, ActivitySignup::class.java)) }
        guest.setOnClickListener{
            getSharedPreferences("userSp", Context.MODE_PRIVATE).edit().clear().commit()

            startActivity(Intent(this, MainActivity::class.java))}
        google.setOnClickListener{startActivity(Intent(this,GoogleLoginActivity::class.java))}
        loginBtn.setOnClickListener {
            performAuth()
        }

        if(firebaseAuth.currentUser != null){
            val user  = firebaseAuth.currentUser!!.email.toString()
            println("debug: check firebase user $user " )
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("email", firebaseAuth.currentUser!!.email.toString())
            startActivity(intent)
        }


    }

    private fun performAuth() {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        progressDialog = ProgressDialog(this)

        if(!email.matches(emailPtn.toRegex())){
            inputEmail.setError("Please enter a valid email")
        }
        else if (password.isEmpty() || password.toString().length < 6){
            inputPassword.setError("Please enter a proper password")
        }
        else {
            progressDialog.setMessage("Please wait while login ...")
            progressDialog.setTitle("Login")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener({
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    sendUserToNextActivity()
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("email",inputEmail.text.toString())
        startActivity(intent)
    }

//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
//    }
//}
//
///**
// * Extension function to simplify setting an afterTextChanged action to EditText components.
// */
//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
}