package com.app.shopngo.RoomDatabase.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.shopngo.RoomDatabase.Model.HistoryEntity

@Dao
interface HistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryEntity)

    @Update
    fun update(item: HistoryEntity): Int

    @Delete
    fun delete(item: HistoryEntity)

    @Query("SELECT * FROM HISTORY ORDER BY ID ASC")
    fun getAllHistoryItems(): LiveData<List<HistoryEntity>>
}