package com.arashjahani.cryptoportfolioapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arashjahani.cryptoportfolioapp.data.DataRepository
import com.arashjahani.cryptoportfolioapp.data.entity.RequestStatus
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val dataRepository: DataRepository): ViewModel(){

    var status= MutableStateFlow<RequestStatus>(RequestStatus.LOADING)

    var symbolsList=MutableStateFlow<List<Symbol>>(ArrayList())

    fun fetchTopCoins(){

        viewModelScope.launch {
            status.emit(RequestStatus.LOADING)

            val response=dataRepository.getTopCoins(20)

            if(response.isSuccessful){
                response.body()?.mData?.let {
                    status.emit(RequestStatus.SUCCESS)
                    symbolsList.emit(it)
                }
            }else{
                status.emit(RequestStatus.FAILED)
            }
        }
    }

    fun searchByKeywords(keywords:String){
        viewModelScope.launch {

            status.emit(RequestStatus.LOADING)

            val response=dataRepository.searchCoinByKeyword(keywords)

            if(response.isSuccessful){
                response.body()?.mData?.let{
                    status.emit(RequestStatus.SUCCESS)
                    symbolsList.emit(it)
                }
            }else{
                status.emit(RequestStatus.FAILED)
            }
        }
    }

}