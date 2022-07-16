package com.saer.login.ui

import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.repositories.PHONE_NUMBER_INVALID_EXCEPTION
import com.saer.login.repositories.TOO_MANY_REQUESTS_EXCEPTION

interface EnterPhoneUi {

    fun apply(
        sendCodeButton: FloatingActionButton,
        phoneTitle: TextView,
        resources: Resources
    )

    class WaitEnterPhoneUi : EnterPhoneUi {

        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            resources: Resources
        ) {
//            sendCodeButton.visibility = View.GONE
            phoneTitle.text = resources.getString(resId = R.string.your_phone_number)
        }
    }

    class SendCodeUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            resources: Resources
        ) {
            sendCodeButton.findNavController()
                .navigate(R.id.action_enterPhoneNumberFragment_to_enterCodeFragment)
        }
    }

    class CompleteEnterPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            resources: Resources
        ) {
//            sendCodeButton.visibility = View.VISIBLE
            phoneTitle.text = resources.getString(R.string.click_send_code)
        }
    }

    class ErrorPhoneUi(private val throwable: Throwable) : EnterPhoneUi {
        override fun apply(
            sendCodeButton: FloatingActionButton,
            phoneTitle: TextView,
            resources: Resources
        ) {
            throwable.message?.let { throwableMessage ->
                var message = ""

                if (throwableMessage.contains(TOO_MANY_REQUESTS_EXCEPTION)) {
                    val countTime: Int = throwableMessage.substringAfter(TOO_MANY_REQUESTS_EXCEPTION).toInt()
                    val countTimeWithStr =
                        if (countTime >= 3600) "${countTime / 3600} ${resources.getString(R.string.hours)}"
                        else if (countTime >= 60) "${countTime / 60} ${resources.getString(R.string.minutes)}"
                        else "$countTime ${resources.getString(R.string.seconds)}"

                    message = "${resources.getString(R.string.too_many_requests)} $countTimeWithStr"
                } else {
                    when (throwableMessage) {
                        PHONE_NUMBER_INVALID_EXCEPTION -> message = resources.getString(R.string.invalid_phone_number)
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