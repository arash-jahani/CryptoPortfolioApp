package com.arashjahani.cryptoportfolioapp.ui.search

import com.arashjahani.cryptoportfolioapp.data.entity.Symbol

interface SearchedItemClickListener {

    fun onSymbolClick(symbol: Symbol)
}