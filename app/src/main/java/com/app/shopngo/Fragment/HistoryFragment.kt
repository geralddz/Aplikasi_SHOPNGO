package com.app.shopngo.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.Adapter.CartAdapter
import com.app.shopngo.Adapter.HistoryAdapter
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.AppDatabase
import com.app.shopngo.RoomDatabase.Model.HistoryEntity
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartRepository
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModel
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartViewModelFactory
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryRepository
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryViewModel
import com.app.shopngo.RoomDatabase.ViewModel.History.HistoryViewModelFactory
import com.google.firebase.database.FirebaseDatabase
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.uikit.SdkUIFlowBuilder


class HistoryFragment : Fragment() {
    private lateinit var itemsRV: RecyclerView
    private lateinit var listhistory: List<HistoryEntity>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val historyRepository = HistoryRepository(AppDatabase(context!!))
        val factory = HistoryViewModelFactory(historyRepository)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        historyViewModel.allhistoryItems().observe(viewLifecycleOwner) { it ->
            historyAdapter.listhistory = it
            historyAdapter.notifyDataSetChanged()
        }
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemsRV = view.findViewById(R.id.rv_history)
        listhistory = ArrayList()
        historyAdapter = HistoryAdapter(listhistory)
        itemsRV.layoutManager = LinearLayoutManager(context)
        itemsRV.adapter = historyAdapter
        val historyRepository = HistoryRepository(AppDatabase(context!!))
        val factory = HistoryViewModelFactory(historyRepository)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        historyViewModel.allhistoryItems().observe(viewLifecycleOwner) { it ->
            historyAdapter.listhistory = it
            historyAdapter.notifyDataSetChanged()
        }
    }


}