package com.saer.telegramnew.auth.ui

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.saer.telegramnew.R
import com.saer.telegramnew.auth.interactors.UNAUTHORIZED_EXCEPTION

interface RegisterUi {

    fun apply(
        context: Context,
        enterFirstNameEditText: EditText,
        enterLastNameEditText: EditText
    )

    class WaitEnterNameUi : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            enterFirstNameEditText.isEnabled = true
            enterLastNameEditText.isEnabled = true
        }
    }

    class EnterFirstNameUi : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {

        }
    }

    class CheckingNameUi : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            enterFirstNameEditText.isEnabled = false
            enterLastNameEditText.isEnabled = false
        }
    }

    class SuccessRegisterUi : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    class NameIsBusyUi : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            Toast.makeText(context, "Busy", Toast.LENGTH_SHORT).show()
        }
    }

    class ErrorUi(
        private val throwable: Throwable
    ) : RegisterUi {
        override fun apply(
            context: Context,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            throwable.message?.let { throwableMessage ->
                val message = when (throwableMessage) {
                    UNAUTHORIZED_EXCEPTION -> context.getString(R.string.unauthorized)
                    else -> ""
                }
                Snackbar.make(
                    enterFirstNameEditText,
                    message.ifEmpty { throwableMessage },
                    Snackbar.LENGTH_LONG
                ).show()
            }
            enterFirstNameEditText.isEnabled = true
            enterLastNameEditText.isEnabled = true
        }
    }

}
