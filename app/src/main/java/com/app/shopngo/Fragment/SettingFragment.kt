package com.app.shopngo.Fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.shopngo.Activity.EditProfileActivity
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class SettingFragment : Fragment() {
    private lateinit var imgprofile: ImageView
    private lateinit var tvuser: TextView
    private lateinit var tvmail: TextView
    private lateinit var tvnama: TextView
    private lateinit var tvalamat: TextView
    private lateinit var tvhp: TextView
    private lateinit var btnedit: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imgprofile = view.findViewById(R.id.imageprofil)
        tvmail = view.findViewById(R.id.tvmail)
        tvuser = view.findViewById(R.id.tvUser)
        tvnama = view.findViewById(R.id.tvnamalkp)
        tvalamat = view.findViewById(R.id.tvAlamat)
        tvhp = view.findViewById(R.id.tvhp)
        btnedit = view.findViewById(R.id.bteditprofile)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
            getData()
            getUserProfile()
        }


        btnedit.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }


    }

    private fun getData() {
        val user = auth.currentUser
        if (user != null) {
            tvmail.text = user.email

        }
    }

    private fun getUserData() {
        this.activity?.let { CustomDialog.showLoading(it) }
        databaseReference.child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful()) {
                if (task.getResult()?.exists() == true) {
                    val dataSnapshot: DataSnapshot = task.result!!
                    val username = dataSnapshot.child("username").value.toString()
                    val nama = dataSnapshot.child("nama").value.toString()
                    val alamat = dataSnapshot.child("alamat").value.toString()
                    val nope = dataSnapshot.child("nope").value.toString()
                    tvuser.text = username
                    tvnama.text = nama
                    tvalamat.text = alamat
                    tvhp.text = nope
                }
            }
        }

    }

    private fun getUserProfile() {
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")
        val localFile = File.createTempFile("tempImage", "jpeg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imgprofile.setImageBitmap(bitmap)
            CustomDialog.hideLoading()
        }.addOnFailureListener {
            CustomDialog.hideLoading()

        }

    }

}
