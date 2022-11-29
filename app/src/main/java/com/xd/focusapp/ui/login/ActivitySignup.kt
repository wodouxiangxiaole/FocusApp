package com.xd.focusapp.ui.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xd.focusapp.Database
import com.xd.focusapp.R
import com.xd.focusapp.databinding.ActivityLoginBinding
import com.xd.focusapp.databinding.ActivitySignupBinding

class ActivitySignup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var inputConfirmPassword: EditText
    lateinit var inputName: EditText
    lateinit var toLogin: TextView
    lateinit var registerBtn: Button
    val emailPtn ="[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-zA-Z]+"
    lateinit var progressDialog: ProgressDialog
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toLogin = binding.toLoginTv
        inputName = binding.inputNameSignup
        inputEmail = binding.inputEmailSignup
        inputPassword = binding.inputPassword
        inputConfirmPassword = binding.inputConfirmPassword
        registerBtn = binding.signupButton
        firebaseAuth = Firebase.auth
        db = Database()

        toLogin.setOnClickListener{ startActivity(Intent(this, ActivityLogin::class.java)) }
        registerBtn.setOnClickListener(){
            PerformAuthentication();
        }
    }

    private fun PerformAuthentication(){
        val name = inputName.text.toString()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        val confirmPassword = inputConfirmPassword.text.toString()
        progressDialog = ProgressDialog(this)
        if(!email.matches(emailPtn.toRegex())){
            inputEmail.setError("Please enter a valid email")
        }
        else if (password.isEmpty() || password.toString().length < 6){
            inputPassword.setError("Please enter a proper password")
        }
        else if (!password.equals(confirmPassword)){
            inputConfirmPassword.setError("Passwords don't match")
        }
        else{
            progressDialog.setMessage("Signing up ...")
            progressDialog.setTitle("Signup")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

//            firebaseAuth = FirebaseAuth.getInstance()
//            firebaseUser = firebaseAuth.currentUser!!

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    firebaseAuth = FirebaseAuth.getInstance()
                    firebaseUser = firebaseAuth.currentUser!!
                    val UID = firebaseUser.getUid()
                    val thread: Thread = Thread({updateDataBase(name, email, password, UID)})
                    thread.start()

                    sendUserToNextActivity()
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateDataBase(name:String, email:String, pw:String, UID:String) {
        val query: String = "INSERT INTO users (user_name, email, pwd, fb_uid) VALUES ('$name','$email','$pw', '$UID')"
        println("Debug: query $query")
        db.insert(query)
        println("Debug: after insert")
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this, ActivityLogin::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}