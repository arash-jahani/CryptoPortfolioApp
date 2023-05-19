package com.arashjahani.cryptoportfolioapp.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

fun String?.parseNumberStringToDouble():Double?{
    if(this.isNullOrEmpty()){
        return null
    }

    val sanitizedString=this
        .replace("$","")
        .replace("%","")
        .replace(",","")
        .trim()

    return sanitizedString.toDoubleOrNull() ?: 0.0
}

fun Double?.toCurrencyFormat(_decimalPlace:Int?=null):String{

    if(this==null){
        return "0"
    }

    var decimalPlace=_decimalPlace
    if(this.hasDecimalPlaces()){
        if(decimalPlace==null){
         decimalPlace=getDecimalPlace(this)
        }
    }else{
        decimalPlace=0
    }

    val roundedNumber=BigDecimal(this).setScale(decimalPlace,RoundingMode.HALF_UP)
    var decimalFormat=DecimalFormat("###,###,###.########")

    return decimalFormat.format(roundedNumber)

}

fun Float?.toCurrencyFormat():String{
    return this?.toDouble().toCurrencyFormat()
}

fun getDecimalPlace(number:Double):Int{
    if(number>1)
        return 2
    else if(number > 0.01)
        return 3
    else if(number > 0.001)
        return 4
    else if(number > 0.0001)
        return 5
    else if(number > 0.00001)
        return 6
    else if(number > 0.000001)
        return 7
    else if(number > 0.0000001)
        return 8
    else if(number > 0.00000001)
        return 9
    else if(number > 0.000000001)
        return 10
    else if(number > 0.0000000001)
        return 11
    else
        return 2
}

fun Double.hasDecimalPlaces():Boolean{
   val bigDecimal=BigDecimal(this)
   val scale=bigDecimal.scale()
   return scale>0

}