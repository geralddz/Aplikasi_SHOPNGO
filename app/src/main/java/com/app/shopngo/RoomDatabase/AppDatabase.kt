package com.app.shopngo.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.shopngo.RoomDatabase.DAO.CartDAO
import com.app.shopngo.RoomDatabase.DAO.HistoryDAO
import com.app.shopngo.RoomDatabase.Model.CartEntity
import com.app.shopngo.RoomDatabase.Model.HistoryEntity

@Database(entities = [CartEntity::class, HistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun cartDAO() : CartDAO
    abstract fun historyDAO() : HistoryDAO
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppsDatabase.db").build()
    }
}