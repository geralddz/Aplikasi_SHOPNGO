package com.app.shopngo.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.shopngo.FirebaseData.UserData
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {
    private lateinit var ivphoto: ImageView
    private lateinit var tvpoto: TextView
    private lateinit var etUsername: EditText
    private lateinit var etNama: EditText
    private lateinit var etalamat: EditText
    private lateinit var ethp: EditText
    private lateinit var imageUri: Uri
    private lateinit var toolbarUp: Toolbar
    private lateinit var btnsave: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    companion object {
        const val RC_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        toolbarUp = findViewById(R.id.tbHome)
        ivphoto = findViewById(R.id.ivphoto)
        tvpoto = findViewById(R.id.tvpoto)
        etUsername = findViewById(R.id.etusername)
        etNama = findViewById(R.id.etnama)
        etalamat = findViewById(R.id.etaddress)
        ethp = findViewById(R.id.etphone)
        btnsave = findViewById(R.id.btnsaveprofile)

        initActionBar()

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")


        toolbarUp.setNavigationOnClickListener {
            finish()
        }
        tvpoto.setOnClickListener {

            selectimage()
        }

        btnsave.setOnClickListener {
            CustomDialog.showLoading(this)
            val username = etUsername.text.toString().trim()
            val nama = etNama.text.toString().trim()
            val alamat = etalamat.text.toString().trim()
            val nope = ethp.text.toString().trim()

            val userData = UserData(username, nama, alamat, nope)
            if (uid != null) {
                databaseReference.child(uid).setValue(userData).addOnCompleteListener {
                    if (it.isSuccessful) {
                        uploadImage()
                    }
                }
            }

        }
    }

    private fun selectimage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RC_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            ivphoto.setImageURI(imageUri)

        }
    }

    private fun uploadImage() {

        storageReference =
            FirebaseStorage.getInstance().getReference("Users/" + auth.currentUser?.uid)
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Berhasil Terupdate", Toast.LENGTH_SHORT).show()
                CustomDialog.hideLoading()
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Profile Gagal Terupdate", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initActionBar() {
        setSupportActionBar(toolbarUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lengkapi Profile"
    }
}