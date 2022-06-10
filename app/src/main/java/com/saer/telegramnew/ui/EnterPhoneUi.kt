package com.saer.telegramnew.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saer.telegramnew.R
import com.saer.telegramnew.interactors.TOO_MANY_REQUESTS_EXCEPTION

interface EnterPhoneUi {

    fun apply(
        sendCodeButton: FloatingActionButton,
        phoneTitle: TextView,
        context: Context
    )

    class WaitInputPhoneUi : EnterPhoneUi {

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

    class CompleteInputPhoneUi : EnterPhoneUi {
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
            throwable.message?.let {
                var errorMessage = it

                if (errorMessage.contains(TOO_MANY_REQUESTS_EXCEPTION)) {
                    val countTime: Int = errorMessage.substringAfter(TOO_MANY_REQUESTS_EXCEPTION).toInt()
                    val countTimeWithStr =
                        if (countTime >= 3600) "${countTime / 3600} ${context.getString(R.string.hours)}"
                        else if (countTime >= 60) "${countTime / 60} ${context.getString(R.string.minutes)}"
                        else "$countTime ${context.getString(R.string.seconds)}"

                    errorMessage = "${context.getString(R.string.too_many_requests)} $countTimeWithStr"
                }

                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}