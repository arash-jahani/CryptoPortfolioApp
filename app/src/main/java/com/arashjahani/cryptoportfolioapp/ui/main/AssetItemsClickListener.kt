package com.arashjahani.cryptoportfolioapp.ui.main

import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction

interface AssetItemsClickListener {

    fun onAssetClick(asset:AssetTransaction)

    fun addNewTransactionClick()
}