package com.xd.focusapp.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R

class GoogleLoginActivity : ActivityLogin() {
    lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    //private lateinit var auth: FirebaseAuth
    //private lateinit var firebaseUser: FirebaseUser
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)
        oneTapClient = Identity.getSignInClient(this)
        firebaseAuth = Firebase.auth

        //Configure the One Tap sign-in client
        //https://developers.google.com/identity/one-tap/android/get-saved-credentials#1_configure_the_one_tap_sign-in_client
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        Toast.makeText(this,"signInWithCredential:success", Toast.LENGTH_SHORT).show()
                                        val user = firebaseAuth.currentUser
                                        updateUI(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(this,"signInWithCredential:failure\n"+task.exception, Toast.LENGTH_SHORT).show()
                                        updateUI(null)
                                    }
                                }
                        }

                        else -> {
                            // Shouldn't happen.
                            Toast.makeText(this,"No ID token", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e: Exception){
                    Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


}