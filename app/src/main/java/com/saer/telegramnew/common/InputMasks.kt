package com.saer.telegramnew.common

import android.content.Context
import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.saer.telegramnew.R

fun setPhoneNumberMask(editText: EditText, context: Context) {
    val formats = ArrayList<String>()
    formats.add(context.getString(R.string.phone_number_musk1))
    val maskedTextChangedListener = MaskedTextChangedListener.installOn(
        editText,
        context.getString(R.string.phone_number_musk2),
        formats,
        AffinityCalculationStrategy.PREFIX
    )
    editText.hint = maskedTextChangedListener.placeholder()
}