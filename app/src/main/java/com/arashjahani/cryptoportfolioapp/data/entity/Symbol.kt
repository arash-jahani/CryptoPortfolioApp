package com.arashjahani.cryptoportfolioapp.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Symbol(

    @SerializedName("i")
    var id:String,

    @SerializedName("ic")
    var icon:String?,

    @SerializedName("n")
    var name:String,

    @SerializedName("s")
    var shortName:String,

    @SerializedName("pu")
    var lastPrice:Double?,

    @SerializedName("p24")
    var change24:Float?

) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Float::class.java.classLoader) as? Float
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(icon)
        parcel.writeString(name)
        parcel.writeString(shortName)
        parcel.writeValue(lastPrice)
        parcel.writeValue(change24)
    }

    companion object CREATOR : Parcelable.Creator<Symbol> {
        override fun createFromParcel(parcel: Parcel): Symbol {
            return Symbol(parcel)
        }

        override fun newArray(size: Int): Array<Symbol?> {
            return arrayOfNulls(size)
        }
    }

}

