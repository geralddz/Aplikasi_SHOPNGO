package com.app.shopngo.RoomDatabase.ViewModel.History

import androidx.lifecycle.ViewModel
import com.app.shopngo.RoomDatabase.Model.CartEntity
import com.app.shopngo.RoomDatabase.Model.HistoryEntity
import com.app.shopngo.RoomDatabase.ViewModel.Cart.CartRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun insert(item: HistoryEntity) = GlobalScope.launch {
        repository.insert(item)
    }

    fun update(item: HistoryEntity) = GlobalScope.launch {
        repository.update(item)
    }

    // In coroutines thread delete item in delete function.
    fun delete(item: HistoryEntity) = GlobalScope.launch {
        repository.delete(item)
    }

    //Here we initialized allGroceryItems function with repository
    fun allhistoryItems() = repository.allhistoryItems()
}