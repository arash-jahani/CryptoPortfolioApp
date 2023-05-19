package com.arashjahani.cryptoportfolioapp.data.entity

import com.google.gson.annotations.SerializedName

data class WrapperResponse<T>(

    @SerializedName("coins")
    var mData:T?=null
)
