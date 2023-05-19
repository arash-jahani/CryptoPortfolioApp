package com.arashjahani.cryptoportfolioapp.data.network

import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import com.arashjahani.cryptoportfolioapp.data.entity.WrapperResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinStatsApiService {

    @GET("v4/coins")
    suspend fun getCoinsByIds(@Query("ids") ids:String) : Response<WrapperResponse<List<Symbol>>>

    @GET("v2/coins")
    suspend fun getTopCoins(@Query("limit") limit:Int):Response<WrapperResponse<List<Symbol>>>

    @GET("v2/coins")
    suspend fun searchCoinByKeyword(@Query("keyword") keyword:String) :Response<WrapperResponse<List<Symbol>>>

}