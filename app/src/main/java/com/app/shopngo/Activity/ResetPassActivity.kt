package com.app.shopngo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.shopngo.R
import com.app.shopngo.Notification.NotificationChannnel
import com.google.firebase.auth.FirebaseAuth

class ResetPassActivity : AppCompatActivity() {
    private lateinit var toolbarreset: Toolbar
    private lateinit var etEmailreset : EditText
    private lateinit var btnSendMail : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        toolbarreset = findViewById(R.id.tbForgotPass)
        etEmailreset = findViewById(R.id.etEmailResetPass)
        btnSendMail = findViewById(R.id.btnSendEmail)



        initActionBar()
        initFirebase()

        btnSendMail.setOnClickListener {
            val email = etEmailreset.text.toString().trim()
            if(email.isEmpty()) {
                etEmailreset.error = "Masukkan Email Anda"
                etEmailreset.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailreset.error = "Masukkan Email dengan Benar"
                etEmailreset.requestFocus()
                return@setOnClickListener
            }else{
                forgotpass(email)
            }
        }

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun forgotpass(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val builder = NotificationCompat.Builder(this, NotificationChannnel.CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.shopgo)
                        .setContentTitle("Succeed")
                        .setContentText("Your reset password has been sent to your email")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    val notification = builder.build()
                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(1,notification)
                    Toast.makeText(this, "Your reset password has been sent to your email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Pastikan Email Sudah Benar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initActionBar() {
        setSupportActionBar(toolbarreset)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}