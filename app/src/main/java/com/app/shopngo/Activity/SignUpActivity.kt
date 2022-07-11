package com.app.shopngo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var toolbarUp: Toolbar
    private lateinit var btnSignUp : Button
    private lateinit var etEmailUp : EditText
    private lateinit var etPassUp : EditText
    private lateinit var etCPassUp : EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        toolbarUp = findViewById(R.id.tbSignUp)
        btnSignUp = findViewById(R.id.btnSignUp)
        etEmailUp = findViewById(R.id.etEmailSignUp)
        etPassUp = findViewById(R.id.etPasswordSignUp)
        etCPassUp = findViewById(R.id.etConfirmPasswordSignUp)

        initActionBar()
        initFirebase()

        btnSignUp.setOnClickListener {
            val email = etEmailUp.text.toString().trim()
            val pass = etPassUp.text.toString().trim()
            val cpass = etCPassUp.text.toString().trim()
            CustomDialog.showLoading(this)
            if (CheckValidation(email, pass, cpass)){
                registerToServer(email, pass)
            }
        }

        toolbarUp.setNavigationOnClickListener {
            finish()
        }

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun registerToServer(email: String, pass: String) {
            auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener { task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Sign Up Berhasil", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            }
            .addOnFailureListener { exception ->
                CustomDialog.hideLoading()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun CheckValidation(email: String, pass: String, cpass: String): Boolean {
       if(email.isEmpty()) {
            etEmailUp.error = "Masukkan Email Anda"
            etEmailUp.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailUp.error = "Masukkan Email dengan Benar"
            etEmailUp.requestFocus()
        } else if (pass.isEmpty()) {
            etPassUp.error = "Masukkan Password Anda"
            etPassUp.requestFocus()
        } else if (cpass.isEmpty()) {
            etCPassUp.error = "Masukkan Konfirmasi Password Anda"
            etCPassUp.requestFocus()
        } else if (pass != cpass) {
            etPassUp.error = "Password Anda Salah"
            etCPassUp.error = "Konfirmasi Password Anda Salah"
            etPassUp.requestFocus()
            etCPassUp.requestFocus()
        } else {
            etPassUp.error = null
            etCPassUp.error = null
            return true
        }
        CustomDialog.hideLoading()
        return false
    }

    private fun initActionBar() {
        setSupportActionBar(toolbarUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}