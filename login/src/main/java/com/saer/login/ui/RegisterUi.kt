package com.saer.login.ui

import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.repositories.UNAUTHORIZED_EXCEPTION

interface RegisterUi {

    fun apply(
        resources: Resources,
        enterFirstNameEditText: EditText,
        enterLastNameEditText: EditText
    )

    class WaitEnterNameUi : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            enterFirstNameEditText.isEnabled = true
            enterLastNameEditText.isEnabled = true
        }
    }

    class EnterFirstNameUi : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {

        }
    }

    class CheckingNameUi : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            enterFirstNameEditText.isEnabled = false
            enterLastNameEditText.isEnabled = false
        }
    }

    class SuccessRegisterUi : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            Toast.makeText(enterFirstNameEditText.context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    class NameIsBusyUi : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            Toast.makeText(enterFirstNameEditText.context, "Busy", Toast.LENGTH_SHORT).show()
        }
    }

    class ErrorUi(
        private val throwable: Throwable
    ) : RegisterUi {
        override fun apply(
            resources: Resources,
            enterFirstNameEditText: EditText,
            enterLastNameEditText: EditText
        ) {
            throwable.message?.let { throwableMessage ->
                val message = when (throwableMessage) {
                    UNAUTHORIZED_EXCEPTION -> resources.getString(R.string.unauthorized)
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
