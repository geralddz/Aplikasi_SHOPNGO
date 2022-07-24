package com.app.shopngo.RoomDatabase.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.app.shopngo.RoomDatabase.Model.CartEntity

@Dao
interface CartDAO {

    @Insert(onConflict = REPLACE)
    fun insert(item: CartEntity)

    @Update
    fun update(item: CartEntity): Int

    @Delete
    fun delete(item: CartEntity)

    @Query("SELECT * FROM CART ORDER BY ID ASC")
    fun getAllCartItems(): LiveData<List<CartEntity>>

}
