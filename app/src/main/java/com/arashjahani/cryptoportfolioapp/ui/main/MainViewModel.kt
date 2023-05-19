package com.arashjahani.cryptoportfolioapp.ui.main

import androidx.lifecycle.ViewModel
import com.arashjahani.cryptoportfolioapp.data.DataRepository
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataRepository: DataRepository) :ViewModel(){

    var totalValue=MutableStateFlow<Double>(0.0)
    var totalValueProgitPercent= MutableStateFlow<Double>(0.0)

    fun getAssets(): Flow<List<AssetTransaction>> =
        dataRepository.getAssets().flatMapConcat { transactions ->
            flow{

                var indexes:String = transactions.joinToString(separator = ","){it.symbolId}

                if(indexes.isNullOrEmpty()){
                    return@flow
                }

                var response=dataRepository.getCoinsByIds(indexes)

                if(response.isSuccessful){
                    transactions.map { _transactionItem ->
                        response.body()?.mData?.find { it.id==_transactionItem.symbolId }?.let {
                            _transactionItem.lastPrice = it.lastPrice
                            _transactionItem.lastPriceChange=it.change24?.toDouble()
                        }
                    }
                }

                calcTotalValue(transactions)

                emit(transactions)

            }
        }

    private suspend fun calcTotalValue(items:List<AssetTransaction>){
        var currentTotal=0.0
        var boughtTotal=0.0
        var profit=0.0

        items.map{
            currentTotal+=it.getCurrentWorth()
            boughtTotal+=it.getBuyWorth()
        }

        profit=(currentTotal-boughtTotal)*100/boughtTotal

        totalValue.emit(currentTotal)

        totalValueProgitPercent.emit(profit)
    }

}