package com.saer.login.ui

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.databinding.FragmentEnterPhoneNumberBinding
import com.saer.login.repositories.PHONE_NUMBER_INVALID_EXCEPTION
import com.saer.login.repositories.TOO_MANY_REQUESTS_EXCEPTION

interface EnterPhoneUi {

    fun apply(
        binding: FragmentEnterPhoneNumberBinding,
        resources: Resources,
    )

    class WaitEnterPhoneUi : EnterPhoneUi {

        override fun apply(binding: FragmentEnterPhoneNumberBinding, resources: Resources) {
            binding.apply {
                sendCodeButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                enterPhoneNumberTitle.text = resources.getString(resId = R.string.your_phone_number)
                inputPhoneNumber.isEnabled = true
            }
        }
    }

    class NavigateToEnterCodeUi(private val phoneNumber: String) : EnterPhoneUi {
        override fun apply(binding: FragmentEnterPhoneNumberBinding, resources: Resources) {
            binding.sendCodeButton.findNavController()
                .navigate(EnterPhoneNumberFragmentDirections.actionEnterPhoneNumberFragmentToEnterCodeFragment(phoneNumber))
        }
    }

    class PendingResultSendingPhoneUi : EnterPhoneUi {
        override fun apply(binding: FragmentEnterPhoneNumberBinding, resources: Resources) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                sendCodeButton.visibility = View.INVISIBLE
                inputPhoneNumber.isEnabled = false
            }
        }
    }

    class PhoneIsNotComplete : EnterPhoneUi {
        override fun apply(binding: FragmentEnterPhoneNumberBinding, resources: Resources) {

        }
    }

    class ErrorPhoneUi(private val throwable: Throwable) : EnterPhoneUi {
        override fun apply(binding: FragmentEnterPhoneNumberBinding, resources: Resources) {
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
                    binding.enterPhoneNumberTitle,
                    message.ifEmpty { throwableMessage },
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.apply {
                sendCodeButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                enterPhoneNumberTitle.text = resources.getString(resId = R.string.your_phone_number)
                inputPhoneNumber.isEnabled = true
            }
        }
    }
}