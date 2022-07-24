package com.app.shopngo.RoomDatabase.ViewModel.Cart

import com.app.shopngo.RoomDatabase.AppDatabase
import com.app.shopngo.RoomDatabase.Model.CartEntity

class CartRepository(private val db: AppDatabase) {
    fun insert(item: CartEntity) = db.cartDAO().insert(item)
    fun delete(item: CartEntity) = db.cartDAO().delete(item)
    fun update(item: CartEntity) = db.cartDAO().update(item)
    fun allCartItems() = db.cartDAO().getAllCartItems()
}