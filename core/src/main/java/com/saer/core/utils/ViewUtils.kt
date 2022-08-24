package com.saer.core.utils

import android.util.TypedValue
import android.view.View

fun View.getPixels(value: Float): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        resources.displayMetrics
    )

fun <T> lastNotNullIndexFromArray(array: Array<T?>): Int {
    for (index in array.size - 1 downTo 0) {
        if (array[index] != null) return index
    }
    return 0
}