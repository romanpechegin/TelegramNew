package com.saer.core.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard(view: View? = null) {
    val innerView = view ?: getView()
    if (innerView != null && innerView.requestFocus()) {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            innerView,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}

fun Fragment.hideKeyboard() {
    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}