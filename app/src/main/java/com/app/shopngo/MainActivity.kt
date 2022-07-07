package com.app.shopngo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var tvemail : TextView
    private lateinit var toolbar: Toolbar
    private lateinit var bottomnav : BottomNavigationView
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.tbout)
        bottomnav = findViewById(R.id.bottomnav)
        tvemail = findViewById(R.id.tvEmail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initFirebase()
        getData()

        replacefragment(HomeFragment())

        bottomnav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replacefragment(HomeFragment())
                R.id.cart -> replacefragment(CartFragment())
                R.id.scanner -> replacefragment(ScannerFragment())
                R.id.history -> replacefragment(HistoryFragment())
                R.id.setting -> replacefragment(SettingFragment())

            }
            true
        }
    }

    private fun replacefragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
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
            tvemail.text = user.displayName.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btnSignOut->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Peringatan !!! ")
                    .setMessage("Apakah Anda Ingin Keluar ? ")
                    .setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                        auth.signOut()
                        mGoogleSignInClient.signOut()
                        Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignIn::class.java))
                        finish()
                    }.setNegativeButton(
                        "No"
                    ) { dialog: DialogInterface, which: Int -> dialog.cancel() }.show()
            }
        }
        return true
    }
}