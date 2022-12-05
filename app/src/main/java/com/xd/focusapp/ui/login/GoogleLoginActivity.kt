package com.xd.focusapp.ui.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xd.focusapp.Database
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R

class GoogleLoginActivity : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var googleSignInClient: GoogleSignInClient

    lateinit var db: Database
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)
        oneTapClient = Identity.getSignInClient(this)
        println("Debug: oneTapClient")
        firebaseAuth = Firebase.auth
        println("Debug: firebaseAuth")
        //just for testing purposes

        if(firebaseAuth.currentUser != null){
            println("Debug: firebaseAuth.currentUser = ${firebaseAuth.currentUser!!.email.toString()}")
            firebaseAuth.signOut()
            println("Debug: firebaseAuth.signOut ")
        }
        //firebaseAuth.signOut()


        db = Database()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Google login ...")
        progressDialog.show()
//        Configure the One Tap sign-in client
//        https://developers.google.com/identity/one-tap/android/get-saved-credentials#1_configure_the_one_tap_sign-in_client
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                .setSupported(true)
//                .build())
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.your_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()


        //firebaseUser= firebaseAuth.currentUser!!

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        println("Debug:googleSignInClient")
        signIn()

    }

//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
////        val currentUser = firebaseAuth.currentUser
////        if(currentUser != null){
////            firebaseUser = currentUser
////        }
//        val currentUser = firebaseAuth.currentUser
//        updateUI(currentUser)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    when {
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with Firebase.
//                            println("Debug: idToken $idToken")
//                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this) { task ->
//                                    if (task.isSuccessful) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "signInWithCredential:success")
//                                        Toast.makeText(this,"signInWithCredential:success", Toast.LENGTH_SHORT).show()
//                                        val user = firebaseAuth.currentUser
//                                        updateUI(user)
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Toast.makeText(this,"signInWithCredential:failure\n"+task.exception, Toast.LENGTH_SHORT).show()
//                                        updateUI(null)
//                                    }
//                                }
//                        }
//
//                        else -> {
//                            // Shouldn't happen.
//                            Toast.makeText(this,"No ID token", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }catch (e: Exception){
//                    Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(this,"signInWithCredential:success", Toast.LENGTH_SHORT).show()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,"signInWithCredential:failure\n"+task.exception, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                finish()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss()
                    val user = firebaseAuth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss()
                    Toast.makeText(this,""+task.exception, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
       //launcher.launch(signInIntent)
    }

//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            handleResult(task)
//        }
//    }
//
//    private fun handleResult(task: Task<GoogleSignInAccount>) {
//        if (task.isSuccessful) {
//            val account: GoogleSignInAccount? = task.result
//            if (account != null) {
//                updateUI(account)
//            }
//        }
//        else{
//            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
//        }
//    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

//    private fun updateUI(account:GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
//            if (it.isSuccessful) {
//                // Sign in success, update UI with the signed-in user's information
//                intent = Intent(this, MainActivity::class.java)
//                val email = account.email.toString()
//                val name = account.displayName.toString()
//                val id = account.id.toString()
//                val idToken = account.idToken.toString()
//                intent.putExtra("email", email)
//                intent.putExtra("name", name)
//                intent.putExtra("id", id)
//                intent.putExtra("idToken", idToken)
//                println("Debug: email $email, name $name, id $id, idToken $idToken")
//                updateDataBase(name,email,id,idToken)
//                startActivity(intent)
//            }
//        }
//    }

    private fun updateDataBase(name:String, email:String, pw:String, UID:String) {
        val query: String = "INSERT INTO users (user_name, email, pwd, fb_uid) VALUES ('$name','$email','$pw', '$UID')"
        println("Debug: query $query")
        db.insert(query)
        println("Debug: after insert")
    }

    private fun updateUI(user: FirebaseUser?) {
        intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                val email = user!!.email.toString()
                val name = user.displayName.toString()
                val id = user.uid.toString()

                intent.putExtra("email", email)
                intent.putExtra("name", name)
                intent.putExtra("id", id)

                println("Debug: email $email, name $name, id $id")
                val thread = Thread({updateDataBase(name,email,id,id)})
                thread.start()

        startActivity(intent)
        finish()
    }


}