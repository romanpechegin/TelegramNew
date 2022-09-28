package com.saer.login.ui

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.databinding.FragmentEnterCodeBinding
import com.saer.login.repositories.PHONE_CODE_INVALID_EXCEPTION

interface EnterCodeUi {
    fun apply(
        resources: Resources,
        binding: FragmentEnterCodeBinding,
        viewModel: EnterCodeViewModel,
    )

    class WaitCodeUi(private val phone: String = "") : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            binding.sendCodeButton.visibility = View.VISIBLE
            binding.enterCodeDescription.text = binding.root.context?.getString(
                R.string.we_have_sent_sms,
                phone
            )
        }
    }

    class ResendingCodeUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            binding.sendCodeButton.visibility = View.GONE
        }
    }

    class NavigateToEnterPasswordUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            binding.enterCodeEditText.findNavController()
                .navigate(R.id.action_enterCodeFragment_to_enterPasswordFragment)
        }
    }

    class CompleteEnterCodeUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            binding.enterCodeEditText.isEnabled = false
        }
    }

    class SuccessAuthUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {

        }
    }

    class ErrorCodeUi(
        private val throwable: Throwable? = null,
    ) : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            throwable?.message?.let { throwableMessage ->
                var message = ""

                when (throwableMessage) {
                    PHONE_CODE_INVALID_EXCEPTION -> {
                        binding.enterCodeEditText.isError = true
                        binding.enterCodeEditText.clearText()
                        message = resources.getString(R.string.invalid_phone_code)
                    }
                }
                Snackbar.make(
                    binding.enterCodeTitle,
                    message.ifEmpty { throwableMessage },
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.enterCodeEditText.isEnabled = true
        }
    }

    class NavigateToRegistrationUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel,
        ) {
            binding.enterCodeEditText.clearText()
            binding.root.findNavController()
                .navigate(R.id.action_enterCodeFragment_to_registrationFragment)
        }
    }
}