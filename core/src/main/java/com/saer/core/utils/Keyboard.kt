package com.saer.core.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard() {
    if (view != null && view!!.requestFocus()) {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            view,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}