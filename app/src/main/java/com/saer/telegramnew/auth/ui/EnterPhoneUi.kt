package com.saer.telegramnew.auth.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.saer.telegramnew.R
import com.saer.telegramnew.auth.interactors.PHONE_NUMBER_INVALID_EXCEPTION
import com.saer.telegramnew.auth.interactors.TOO_MANY_REQUESTS_EXCEPTION

interface EnterPhoneUi {

    fun apply(
        sendCodeButton: FloatingActionButton,
        phoneTitle: TextView,
        context: Context
    )

    class WaitEnterPhoneUi : EnterPhoneUi {

        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            context: Context
        ) {
            sendCodeButton.visibility = View.GONE
            phoneTitle.text = context.getString(R.string.enter_phone_number)
        }
    }

    class SendCodeUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            context: Context
        ) {
            sendCodeButton.findNavController()
                .navigate(R.id.action_EnterPhoneNumberFragment_to_enterCodeFragment)
        }
    }

    class CompleteEnterPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            context: Context
        ) {
            sendCodeButton.visibility = View.VISIBLE
            phoneTitle.text = context.getString(R.string.click_send_code)
        }
    }

    class ErrorPhoneUi(private val throwable: Throwable) : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            context: Context
        ) {
            throwable.message?.let { throwableMessage ->
                var message = ""

                if (throwableMessage.contains(TOO_MANY_REQUESTS_EXCEPTION)) {
                    val countTime: Int = throwableMessage.substringAfter(TOO_MANY_REQUESTS_EXCEPTION).toInt()
                    val countTimeWithStr =
                        if (countTime >= 3600) "${countTime / 3600} ${context.getString(R.string.hours)}"
                        else if (countTime >= 60) "${countTime / 60} ${context.getString(R.string.minutes)}"
                        else "$countTime ${context.getString(R.string.seconds)}"

                    message = "${context.getString(R.string.too_many_requests)} $countTimeWithStr"
                } else {
                    when (throwableMessage) {
                        PHONE_NUMBER_INVALID_EXCEPTION -> message = context.getString(R.string.invalid_phone_number)
                    }
                }

                Snackbar.make(
                    phoneTitle,
                    message.ifEmpty { throwableMessage },
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}