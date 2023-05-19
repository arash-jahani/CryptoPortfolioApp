package com.arashjahani.cryptoportfolioapp.ui.add

import androidx.lifecycle.ViewModel
import com.arashjahani.cryptoportfolioapp.data.DataRepository
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.data.entity.RequestStatus
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(private val dataRepository: DataRepository):ViewModel() {

    var status=MutableStateFlow<RequestStatus>(RequestStatus.LOADING)

    fun getSymbol(symbolId:String?): Flow<Symbol> = flow{

        if(symbolId.isNullOrEmpty()){
            status.emit(RequestStatus.FAILED)
            return@flow
        }

        status.emit(RequestStatus.LOADING)

        val response = dataRepository.getCoinsByIds(symbolId)

        if(response.isSuccessful){
            response.body()?.mData?.let {
                status.emit(RequestStatus.SUCCESS)
                emit(it.first())
            }
        }else{
            status.emit(RequestStatus.FAILED)
        }
    }

    fun saveTransaction(assetTransaction: AssetTransaction): Flow<Long> = flow{
        val response = dataRepository.saveTransaction(assetTransaction)

        emit(response)
    }.catch {

    }.flowOn(Dispatchers.IO)

}
