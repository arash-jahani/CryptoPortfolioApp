package com.arashjahani.cryptoportfolioapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction

@Database(entities = arrayOf(AssetTransaction::class), version = 1, exportSchema = false)
abstract class AppDatabase :RoomDatabase(){

    abstract fun transactionDao():TransactionDao
}