package com.app.shopngo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var tvemail : TextView
    private lateinit var btnSignOut : Button
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvemail = findViewById(R.id.tvEmail)
        btnSignOut = findViewById(R.id.btnSignOut)
        initFirebase()
        getData()

        btnSignOut.setOnClickListener {
            auth.signOut()
            mGoogleSignInClient.signOut()
            Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun getData() {
        val user = auth.currentUser
        if (user != null){
            tvemail.text = user.email
        }
    }
}