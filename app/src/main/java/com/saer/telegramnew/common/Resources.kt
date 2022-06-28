package com.saer.telegramnew.common

import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes

interface Resources {

    fun getInt(@IntegerRes resId: Int): Int
    fun getString(@StringRes resId: Int): String

    class Base(private val context: Context) : Resources {
        override fun getInt(resId: Int) = context.resources.getInteger(resId)

        override fun getString(resId: Int): String = context.resources.getString(resId)
    }
}