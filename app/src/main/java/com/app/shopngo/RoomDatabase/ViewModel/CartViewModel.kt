package com.app.shopngo.RoomDatabase.ViewModel

import androidx.lifecycle.ViewModel
import com.app.shopngo.RoomDatabase.CartEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    fun insert(item: CartEntity) = GlobalScope.launch {
        repository.insert(item)
    }

    // In coroutines thread delete item in delete function.
    fun delete(item: CartEntity) = GlobalScope.launch {
        repository.delete(item)
    }

    //Here we initialized allGroceryItems function with repository
    fun allCartItems() = repository.allCartItems()
}