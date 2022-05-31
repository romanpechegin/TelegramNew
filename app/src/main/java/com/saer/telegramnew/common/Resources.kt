package com.saer.telegramnew.common

import android.content.Context
import androidx.annotation.IntegerRes

interface Resources {

    fun getInt(@IntegerRes resId: Int): Int

    class Base(private val context: Context) : Resources {
        override fun getInt(resId: Int) = context.resources.getInteger(resId)
    }
}