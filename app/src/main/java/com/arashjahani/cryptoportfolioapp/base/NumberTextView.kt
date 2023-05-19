package com.arashjahani.cryptoportfolioapp.base

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.arashjahani.cryptoportfolioapp.R
import com.arashjahani.cryptoportfolioapp.utils.parseNumberStringToDouble
import com.google.android.material.textview.MaterialTextView

class NumberTextView(context: Context, attrs:AttributeSet?) : MaterialTextView(context,attrs),TextWatcher {

    init {
        addTextChangedListener(this)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        applyColor()

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {

    }

    private fun applyColor(){
        val number=text.toString().parseNumberStringToDouble()
        val color =when{
            number==null || number==0.0 ->context.getColor(R.color.color_e5)
            number >0 ->context.getColor(R.color.color_value_profit)
            else ->context.getColor(R.color.color_value_lose)
        }
        setTextColor(color)
    }
}