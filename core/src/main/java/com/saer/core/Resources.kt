package com.saer.core

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes

interface Resources {

    fun getInt(@IntegerRes resId: Int): Int
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, arg: Any): String
    fun getStringArray(@ArrayRes resId: Int): Array<String>

    class Base(private val context: Context) : Resources {
        override fun getInt(resId: Int) = context.resources.getInteger(resId)

        override fun getString(resId: Int): String = context.resources.getString(resId)
        override fun getString(@StringRes resId: Int, arg: Any): String =
            context.resources.getString(resId, arg)

        override fun getStringArray(resId: Int): Array<String> =
            context.resources.getStringArray(resId)
    }
}