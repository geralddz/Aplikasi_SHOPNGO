package com.app.shopngo.RoomDatabase.ViewModel.Cart

import androidx.lifecycle.ViewModel
import com.app.shopngo.RoomDatabase.Model.CartEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    fun insert(item: CartEntity) = GlobalScope.launch {
        repository.insert(item)
    }

    fun update(item: CartEntity) = GlobalScope.launch {
        repository.update(item)
    }

    // In coroutines thread delete item in delete function.
    fun delete(item: CartEntity) = GlobalScope.launch {
        repository.delete(item)
    }

    //Here we initialized allGroceryItems function with repository
    fun allCartItems() = repository.allCartItems()
}