package com.saer.login.ui

import android.util.Log
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
        viewModel: EnterCodeViewModel
    )

    class WaitCodeUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            binding.enterCodeEditText.isEnabled = true
        }
    }

    class ErrorCodeFormatUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }

    }

    class WaitPasswordUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            Log.e("TAG", "apply: ${javaClass.simpleName}")
        }
    }

    class CompleteEnterCodeUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
//            binding.enterCodeEditText.isEnabled = false
        }
    }

    class SuccessAuthUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }
    }

    class ErrorCodeUi(
        private val throwable: Throwable
    ) : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            throwable.message?.let { throwableMessage ->
                var message = ""

                when (throwableMessage) {
                    PHONE_CODE_INVALID_EXCEPTION -> {
                        binding.enterCodeEditText.isError = true
                        binding.enterCodeEditText.clearText()
                        message = resources.getString(R.string.invalid_phone_code)
                    }
                }
                Snackbar.make(
                    binding.root,
                    message.ifEmpty { throwableMessage },
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.enterCodeEditText.isEnabled = true
        }
    }

    class WaitPhoneUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }
    }

    class EnterNameUi : EnterCodeUi {
        override fun apply(
            resources: Resources,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            binding.enterCodeEditText.clearText()
            binding.root.findNavController()
                .navigate(R.id.action_enterCodeFragment_to_registrationFragment)
        }
    }
}