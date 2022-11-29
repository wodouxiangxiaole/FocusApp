package com.xd.focusapp.ui.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xd.focusapp.Database
import com.xd.focusapp.MainActivity
import com.xd.focusapp.databinding.ActivityLoginBinding

import com.xd.focusapp.R

open class ActivityLogin : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
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

        val db = Database()

        // test
         val query = "select * from users"
         db.getAllUser(query)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputEmail = binding.inputEmailLogin
        inputPassword = binding.inputPasswordLogin
        loginBtn = binding.loginButton
        loading = binding.loading
        loading.visibility = View.INVISIBLE
        guest = binding.GuestLoginBtn
        signUp = binding.noAccountTv
        pwReset = binding.forgetPwTv
        google = binding.googleButton
        firebaseAuth = Firebase.auth

        signUp.setOnClickListener{ startActivity(Intent(this, ActivitySignup::class.java)) }
        guest.setOnClickListener{startActivity(Intent(this, MainActivity::class.java))}
        google.setOnClickListener{startActivity(Intent(this,GoogleLoginActivity::class.java))}
        loginBtn.setOnClickListener {
            performAuth()
        }

//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)
//
//        loginViewModel.loginFormState.observe(this, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both username / password is valid
//            login.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                username.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password.error = getString(loginState.passwordError)
//            }
//        })
//
//        loginViewModel.loginResult.observe(this, Observer {
//            val loginResult = it ?: return@Observer
//
//            loading.visibility = View.GONE
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
//            setResult(Activity.RESULT_OK)
//
//            //Complete and destroy login activity once successful
//            finish()
//        })
//
//        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                username.text.toString(),
//                password.text.toString()
//            )
//        }
//
//        password.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//
//            login.setOnClickListener {
//                loading.visibility = View.VISIBLE
//                loginViewModel.login(username.text.toString(), password.text.toString())
//            }
//        }
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