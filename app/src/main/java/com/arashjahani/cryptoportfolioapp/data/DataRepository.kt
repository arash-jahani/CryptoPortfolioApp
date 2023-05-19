package com.arashjahani.cryptoportfolioapp.data

import com.arashjahani.cryptoportfolioapp.data.local.db.TransactionDao
import com.arashjahani.cryptoportfolioapp.data.network.CoinStatsApiService

interface DataRepository:CoinStatsApiService,TransactionDao {
}