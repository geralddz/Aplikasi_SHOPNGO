package com.app.shopngo.RoomDatabase.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
open class HistoryEntity (

    @ColumnInfo(name = "idtransaksi")
    var idtrans: String,

    @ColumnInfo(name = "total")
    var total: Int,

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}