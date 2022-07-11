package com.app.shopngo.RoomDatabase.ViewModel

import com.app.shopngo.RoomDatabase.AppDatabase
import com.app.shopngo.RoomDatabase.CartEntity

class CartRepository(private val db: AppDatabase) {
    fun insert(item: CartEntity) = db.cartDAO().insert(item)
    fun delete(item: CartEntity) = db.cartDAO().delete(item)
    fun allCartItems() = db.cartDAO().getAllCartItems()
}