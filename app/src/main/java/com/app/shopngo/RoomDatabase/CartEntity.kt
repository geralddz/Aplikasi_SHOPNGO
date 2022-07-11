package com.app.shopngo.RoomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
open class CartEntity (

    @ColumnInfo(name = "nama")
    var nama: String,

    @ColumnInfo(name = "harga")
    var harga: Int,

    @ColumnInfo(name = "jumlah")
    var jumlah : Int

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}