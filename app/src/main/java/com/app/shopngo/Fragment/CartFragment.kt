package com.app.shopngo.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.Adapter.CartAdapter
import com.app.shopngo.Adapter.HistoryAdapter
import com.app.shopngo.Object.CustomDialog
import com.app.shopngo.Object.Helper
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.AppDatabase
import com.app.shopngo.RoomDatabase.Model.CartEntity
import com.app.shopngo.RoomDatabase.Model.HistoryEntity
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartRepository
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModel
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModelFactory
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryRepository
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryViewModel
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillInfoModel
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder


class CartFragment : Fragment(), CartAdapter.CartItemClickInerface {
    private lateinit var itemsRV: RecyclerView
    private lateinit var list: List<CartEntity>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel : CartViewModel
    private lateinit var tvTotal: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var tvdisc: TextView
    private lateinit var verif: ImageView
    private lateinit var btnCekout: Button
    private lateinit var databaseReference : DatabaseReference
    private lateinit var etdisc : EditText
    private lateinit var transactionRequest: TransactionRequest
    private lateinit var listhistory: List<HistoryEntity>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel : HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val historyRepository = HistoryRepository(AppDatabase(context!!))
        val factoryhistory = HistoryViewModelFactory(historyRepository)
        historyViewModel = ViewModelProvider(this,factoryhistory).get(HistoryViewModel::class.java)
        listhistory = ArrayList()
        historyAdapter = HistoryAdapter(listhistory)


        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemsRV = view.findViewById(R.id.rv_cart)
        btnCekout = view.findViewById(R.id.btcekout)
        verif = view.findViewById(R.id.ivverif)
        etdisc = view.findViewById(R.id.etdiskon)
        tvdisc = view.findViewById(R.id.tvdisc)
        tvTotal = view.findViewById(R.id.itemtotal)
        tvTotalBayar = view.findViewById(R.id.tvtotalbayar)

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-tS9N-29dDWF2U7zv") // client_key is mandatory
            .setContext(context)
            .setTransactionFinishedCallback {

            }
            .setMerchantBaseUrl("https://promo-engine-sample-server.herokuapp.com/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()
        databaseReference = FirebaseDatabase.getInstance().getReference("Promo")
        list = ArrayList()
        cartAdapter = CartAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(context)
        itemsRV.adapter = cartAdapter
        val cartRepository = CartRepository(AppDatabase(context!!))
        val factory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this,factory)[CartViewModel::class.java]
        cartViewModel.allCartItems().observe(viewLifecycleOwner) { it ->
            cartAdapter.list = it
            cartAdapter.notifyDataSetChanged()
            var totalHarga = 0
            cartViewModel.allCartItems().observe(viewLifecycleOwner) {
                for (item in it) {
                    if (item.selected) {
                        val harga = Integer.valueOf(item.harga)
                        totalHarga += (harga * item.jumlah)
                    }
                }
                tvTotal.text = Helper().gantiRupiah(totalHarga)
                tvTotalBayar.text = Helper().gantiRupiah(totalHarga)

            }
        }
        verif.setOnClickListener {
            val disc = etdisc.text.toString().trim()
            this.activity?.let { CustomDialog.showLoading(it) }
            databaseReference.child(disc).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                        var diskon = 0.0
                        var dataSnapshot: DataSnapshot = task.result!!
                        if (dataSnapshot.child("disc").value == null) {
                            diskon = 0.0
                        }else{
                            diskon = dataSnapshot.child("disc").value.toString().toDouble()
                        }
                        cartViewModel.allCartItems().observe(viewLifecycleOwner) {
                            var totalHarga = 0
                            var disc = 0
                            var totalbayar =0
                            for (item in it) {
                                if (item.selected) {
                                    val harga = Integer.valueOf(item.harga)
                                    totalHarga += (harga * item.jumlah)
                                    disc = (totalHarga * diskon).toInt()
                                    totalbayar = totalHarga - disc
                                }
                            }

                            tvdisc.text = Helper().gantiRupiah(disc)
                            tvTotalBayar.text = Helper().gantiRupiah(totalbayar)
                            CustomDialog.hideLoading()
                    }
                }
            }
        }

        btnCekout.setOnClickListener {
            val disc = etdisc.text.toString().trim()
            this.activity?.let { CustomDialog.showLoading(it) }
            databaseReference.child(disc).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.exists() == true) {
                        var diskon = 0.0
                        var dataSnapshot: DataSnapshot = task.result!!
                        if (dataSnapshot.child("disc").value == null) {
                            diskon = 0.0
                        }else{
                            diskon = dataSnapshot.child("disc").value.toString().toDouble()
                        }
                        cartViewModel.allCartItems().observe(viewLifecycleOwner) {
                            var totalHarga = 0
                            var disc = 0
                            var totalbayar =0
                            for (item in it) {
                                if (item.selected) {
                                    val harga = Integer.valueOf(item.harga)
                                    totalHarga += (harga * item.jumlah)
                                    disc = (totalHarga * diskon).toInt()
                                    totalbayar = totalHarga - disc
                                }
                            }

                            tvdisc.text = Helper().gantiRupiah(disc)
                            tvTotalBayar.text = Helper().gantiRupiah(totalbayar)

                            transactionRequest = TransactionRequest("Shop-N-Go"+System.currentTimeMillis().toString()+"", totalbayar.toDouble())
                            val customerDetails = CustomerDetails()
                            customerDetails.customerIdentifier = "Gerald-6789"
                            customerDetails.phone = "08123456789"
                            customerDetails.firstName = "Gerald"
                            customerDetails.lastName = "Dzulfiqar"
                            customerDetails.email = "dz_gerald@yahoo.com"

                            val shippingAddress = ShippingAddress()
                            shippingAddress.address = "Jl. Pahlawan No. 11"
                            shippingAddress.city = "Semarang"
                            shippingAddress.postalCode = "50242"
                            customerDetails.shippingAddress = shippingAddress

                            val billingAddress = BillingAddress()
                            billingAddress.address = "Jl. Pahlawan No. 11"
                            billingAddress.city = "Semarang"
                            billingAddress.postalCode = "50242"
                            customerDetails.billingAddress = billingAddress

                            transactionRequest.customerDetails = customerDetails

                            val billInfoModel = BillInfoModel("Shopngo13431432", "Payment")
                            transactionRequest.billInfoModel = billInfoModel

                            MidtransSDK.getInstance().transactionRequest = transactionRequest
                            MidtransSDK.getInstance().startPaymentUiFlow(requireActivity())

                            val id = transactionRequest.orderId.toString()
                            val total = transactionRequest.amount.toInt()
                            if(id.isNotEmpty()){
                            val items = HistoryEntity(id,total)
                            historyViewModel.insert(items)
                            historyAdapter.notifyDataSetChanged()
                            }
                            CustomDialog.hideLoading()
                        }
                    }
                }

            }.addOnFailureListener{
                CustomDialog.hideLoading()
            }
        }

    }
    override fun onDelete(cartEntity: CartEntity) {
        cartViewModel.delete(cartEntity)
        cartAdapter.notifyDataSetChanged()
    }

    override fun onUpdate(cartEntity: CartEntity) {
        cartViewModel.update(cartEntity)
        cartAdapter.notifyDataSetChanged()
    }



}
