package com.app.shopngo.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.Adapter.CartAdapter
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.*
import com.app.shopngo.RoomDatabase.ViewModel.CartRepository
import com.app.shopngo.RoomDatabase.ViewModel.CartViewModel
import com.app.shopngo.RoomDatabase.ViewModel.CartViewModelFactory

class CartFragment : Fragment(), CartAdapter.CartItemClickInerface {
    lateinit var itemsRV: RecyclerView
    lateinit var list: List<CartEntity>
    lateinit var cartAdapter: CartAdapter
    lateinit var cartViewModel : CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        itemsRV = view.findViewById(R.id.rv_cart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = ArrayList()
        cartAdapter = CartAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(context)
        itemsRV.adapter = cartAdapter
        val cartRepository = CartRepository(AppDatabase(context!!))
        val factory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this,factory).get(CartViewModel::class.java)
        cartViewModel.allCartItems().observe(viewLifecycleOwner, Observer{
            cartAdapter.list = it
            cartAdapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(cartEntity: CartEntity) {
        cartViewModel.delete(cartEntity)
        cartAdapter.notifyDataSetChanged()
    }

}
