package com.app.shopngo.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CartEntity)

    @Delete
    fun delete(item: CartEntity)

    @Query("SELECT * FROM CART ORDER BY ID ASC")
    fun getAllCartItems(): LiveData<List<CartEntity>>

}
