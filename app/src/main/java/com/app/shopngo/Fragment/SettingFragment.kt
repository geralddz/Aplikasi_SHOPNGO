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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.shopngo.Activity.EditProfileActivity
import com.app.shopngo.FirebaseData.UserData
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class SettingFragment : Fragment() {
    private lateinit var imgprofile : ImageView
    private lateinit var tvuser : TextView
    private lateinit var tvmail : TextView
    private lateinit var tvnama : TextView
    private lateinit var tvalamat : TextView
    private lateinit var tvhp : TextView
    private lateinit var btnedit : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var uid : String
    private lateinit var userdata : UserData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
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
        if(uid.isNotEmpty()){
            getUserData()
            getData()
        }


        btnedit.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }


    }
    private fun getData() {
        val user = auth.currentUser
        if (user != null){
            tvmail.text = user.email

        }
    }
    private fun getUserData() {
        this.activity?.let { CustomDialog.showLoading(it) }
       databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               userdata = snapshot.getValue(UserData::class.java)!!
               tvuser.text = userdata.username
               tvnama.text = userdata.nama
               tvalamat.text = userdata.alamat
               tvhp.text = userdata.nope
               getUserProfile()
           }

           override fun onCancelled(error: DatabaseError) {
               Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
               startActivity(Intent(activity, EditProfileActivity::class.java))
           }

       })
    }

    private fun getUserProfile() {
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")
        val localFile = File.createTempFile("tempImage","jpeg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imgprofile.setImageBitmap(bitmap)
            CustomDialog.hideLoading()
        }.addOnFailureListener{
            CustomDialog.hideLoading()

        }

    }

}
