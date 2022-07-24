package com.app.shopngo.RoomDatabase.ViewModel.History

import com.app.shopngo.RoomDatabase.AppDatabase

import com.app.shopngo.RoomDatabase.Model.HistoryEntity

class HistoryRepository (private val db: AppDatabase){
    fun insert(item: HistoryEntity) = db.historyDAO().insert(item)
    fun delete(item: HistoryEntity) = db.historyDAO().delete(item)
    fun update(item: HistoryEntity) = db.historyDAO().update(item)
    fun allhistoryItems() = db.historyDAO().getAllHistoryItems()
}