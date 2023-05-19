package com.arashjahani.cryptoportfolioapp.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "transactions")
@Parcelize
data class AssetTransaction(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id:Int=0,

    @ColumnInfo(name="symbol_id")
    var symbolId:String,

    @ColumnInfo(name="symbol")
    var symbol:String,

    @ColumnInfo(name="symbol_short")
    var symbolShort:String,

    @ColumnInfo(name="price")
    var price:Double,

    @ColumnInfo(name="amount")
    var amount:Float,

    @ColumnInfo(name="transaction_type")
    var transaction_type:String,

    @ColumnInfo(name="date")
    var date:String,

    @ColumnInfo(name="icon")
    var icon:String,

    @ColumnInfo(name="total_buy_amount")
    var totalBuyAmount:Double?=0.0,

    @ColumnInfo(name="total_sell_amount")
    var totalSellAmount:Double?=0.0,

    @ColumnInfo(name="total_buy_price")
    var totalBuyPrice:Double?=0.0,

    @ColumnInfo(name="total_sell_price")
    var totalSellPrice:Double?=0.0,

    @Ignore
    var lastPrice:Double?=0.0,

    @Ignore
    var lastPriceChange:Double?=0.0


):Parcelable{

    constructor():this(0,"","","",0.0,0f,"","","")

    fun getCurrentAmount():Double{
        return totalBuyAmount?.minus(totalSellAmount?: 1.0) ?: 0.0
    }

    fun getCurrentWorth():Double{
        return lastPrice?.times(getCurrentAmount()) ?: 0.0
    }

    fun getBuyWorth():Double{
        return getAvgBuy()?.times(getCurrentAmount()) ?: 0.0
    }

    fun getAvgBuy():Double{
        if(totalBuyAmount==0.0)
            return 0.0

        return totalBuyPrice?.div(totalBuyAmount ?: 1.0) ?: 0.0
    }

    fun getAvgSell():Double{
        if(totalSellAmount==0.0)
            return 0.0
        return totalSellPrice?.div(totalSellAmount ?: 1.0) ?: 0.0
    }

    fun getTransactionWorth():Double{
        return price?.times(amount) ?: 0.0
    }

    fun getProfitPercent():Double{
        if(getBuyWorth()==0.0)
            return 0.0//Double.POSITIVE_INFINITY
        else
            return ((getCurrentWorth()-getBuyWorth())*100/getBuyWorth())
    }






}
