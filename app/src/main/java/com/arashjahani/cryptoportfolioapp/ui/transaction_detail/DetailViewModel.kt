package com.arashjahani.cryptoportfolioapp.ui.transaction_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arashjahani.cryptoportfolioapp.data.DataRepository
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel(){

    fun deleteTransaction(id:Int){
        viewModelScope.launch {
            dataRepository.deleteTransaction(id)
        }
    }

    fun getAsset(symbolId:String,lastPrice:Double): Flow<AssetTransaction> =
        dataRepository.getAssets().flatMapConcat { transition ->
            flow{
                transition.find {  it.symbolId==symbolId}?.let {
                    it.lastPrice=lastPrice
                    emit(it)
                }?:run{
                    emit(AssetTransaction())
                }
            }
        }

    fun getAssetTransaction(symbolId:String):Flow<List<AssetTransaction>> = dataRepository.getAssetTransaction(symbolId).flatMapConcat {
        flow{
            emit(it)
        }
    }
}