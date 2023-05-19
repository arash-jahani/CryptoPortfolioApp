package com.arashjahani.cryptoportfolioapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun saveTransaction(assetTransaction: AssetTransaction):Long

    @Transaction
    @Query("SELECT *," +
            " SUM(CASE WHEN transaction_type ='buy' THEN amount ELSE 0 END) AS total_buy_amount," +
            " SUM(CASE WHEN transaction_type ='sell' THEN amount ELSE 0 END) AS total_sell_amount," +

            " SUM(CASE WHEN transaction_type ='buy' THEN amount * price ELSE 0 END) AS total_buy_price," +
            " SUM(CASE WHEN transaction_type ='sell' THEN amount * price ELSE 0 END) AS total_sell_price" +
            " FROM transactions GROUP BY symbol_id ORDER By _id ASC")
    fun getAssets(): Flow<List<AssetTransaction>>

    @Transaction
    @Query("SELECT * FROM transactions where symbol_id=:symbolId")
    fun getAssetTransaction(symbolId:String):Flow<List<AssetTransaction>>

    @Query("DELETE FROM transactions WHERE _id=:id")
    suspend fun deleteTransaction(id:Int)

}