package com.arashjahani.cryptoportfolioapp.data

import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import com.arashjahani.cryptoportfolioapp.data.entity.WrapperResponse
import com.arashjahani.cryptoportfolioapp.data.local.db.TransactionDao
import com.arashjahani.cryptoportfolioapp.data.network.CoinStatsApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val coinStatsApiService: CoinStatsApiService,
    private val transactionDao: TransactionDao

    ):DataRepository {
    override suspend fun getCoinsByIds(ids: String): Response<WrapperResponse<List<Symbol>>> {
        return coinStatsApiService.getCoinsByIds(ids)
    }

    override suspend fun getTopCoins(limit: Int): Response<WrapperResponse<List<Symbol>>> {
        return coinStatsApiService.getTopCoins(limit)
    }

    override suspend fun searchCoinByKeyword(keyword: String): Response<WrapperResponse<List<Symbol>>> {
        return  coinStatsApiService.searchCoinByKeyword(keyword)
    }

    override suspend fun saveTransaction(assetTransaction: AssetTransaction): Long {
        return transactionDao.saveTransaction(assetTransaction)
    }

    override fun getAssets(): Flow<List<AssetTransaction>> {
        return transactionDao.getAssets()
    }

    override fun getAssetTransaction(symbolId: String): Flow<List<AssetTransaction>> {
        return transactionDao.getAssetTransaction(symbolId)
    }

    override suspend fun deleteTransaction(id: Int) {
        return transactionDao.deleteTransaction(id)
    }
}