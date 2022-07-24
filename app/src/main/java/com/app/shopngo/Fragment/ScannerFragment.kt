package com.app.shopngo.Fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.shopngo.Adapter.CartAdapter
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.*
import com.app.shopngo.RoomDatabase.Model.CartEntity
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartRepository
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModel
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModelFactory
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ScannerFragment : Fragment(), CartAdapter.CartItemClickInerface {
    private lateinit var etnamabarang: EditText
    private lateinit var ethargabarang: EditText
    private lateinit var etjumlahbarang: EditText
    private lateinit var codeScanner: CodeScanner
    private lateinit var auth: FirebaseAuth
    private lateinit var ivbarang : ImageView
    private lateinit var btnadd : Button
    private lateinit var databaseReference : DatabaseReference
    private lateinit var nocode : String
    private lateinit var list: List<CartEntity>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel : CartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)
        val cartRepository = CartRepository(AppDatabase(context!!))
        val factory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this,factory).get(CartViewModel::class.java)
        list = ArrayList()
        cartAdapter = CartAdapter(list, this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        etnamabarang = view.findViewById(R.id.etnamabarang)
        ethargabarang = view.findViewById(R.id.ethargabarang)
        etjumlahbarang = view.findViewById(R.id.etjumlahbarang)
        btnadd = view.findViewById(R.id.btaddtocart)

        ivbarang = view.findViewById(R.id.ivbarang)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Item")
        val permission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
        if (permission!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA),1)
        }
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                nocode = it.text
                getItemData()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        btnadd.setOnClickListener {
            val nama = etnamabarang.text.toString()
            val harga = ethargabarang.text.toString()
            val jumlah = etjumlahbarang.text.toString()
            val gambar = ivbarang.toString()
            val qty : Int = jumlah.toInt()
            val price : Int = harga.toInt()
            if(nama.isEmpty()&&harga.isEmpty()&&price==0) {
                etnamabarang.error = "Scan barcode"
                ethargabarang.error = "Scan barcode"
            } else if (jumlah.isEmpty()&&qty==0) {
                etjumlahbarang.error = "Masukkan Jumlah Barang"
            }else{
                val items = CartEntity(nama,price,qty,gambar)
                cartViewModel.insert(items)
                Toast.makeText(context, "barang berhasil ditambahakan", Toast.LENGTH_SHORT).show()
                cartAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun getItemData() {
        this.activity?.let { CustomDialog.showLoading(it) }
        databaseReference.child(nocode).get().addOnCompleteListener { task ->
            if (task.isSuccessful()) {
                if (task.getResult()?.exists() == true) {
                    val dataSnapshot: DataSnapshot = task.result!!
                    val namabarang = dataSnapshot.child("namabarang").value.toString()
                    val harga = dataSnapshot.child("harga").value.toString()
                    val foto = dataSnapshot.child("url").value.toString()
                    etnamabarang.setText(namabarang).toString()
                    ethargabarang.setText(harga).toString()
                    Picasso.get().load(foto).centerCrop().fit().into(ivbarang)
                    CustomDialog.hideLoading()
                }
            }
        }.addOnFailureListener {
            CustomDialog.hideLoading()
        }
    }


    override fun onDelete(cartEntity: CartEntity) {
        TODO("Not yet implemented")
    }

    override fun onUpdate(cartEntity: CartEntity) {
        TODO("Not yet implemented")
    }


}
