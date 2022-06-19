package com.saer.telegramnew.auth.ui

import android.content.Context
import android.util.Log
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.saer.telegramnew.R
import com.saer.telegramnew.auth.interactors.PHONE_CODE_INVALID_EXCEPTION
import com.saer.telegramnew.databinding.FragmentEnterCodeBinding

interface EnterCodeUi {
    fun apply(
        context: Context,
        binding: FragmentEnterCodeBinding,
        viewModel: EnterCodeViewModel
    )

    class WaitCodeUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            binding.enterCodeEditText.isEnabled = true
        }

    }

    class ErrorCodeFormatUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }

    }

    class WaitPasswordUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            Log.e("TAG", "apply: ${javaClass.simpleName}")
        }
    }

    class CompleteEnterCodeUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            binding.enterCodeEditText.isEnabled = false
        }
    }

    class SuccessAuthUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }
    }

    class ErrorCodeUi(
        private val throwable: Throwable
    ) : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            throwable.message?.let { throwableMessage ->
                var message = ""

                when (throwableMessage) {
                    PHONE_CODE_INVALID_EXCEPTION -> message =
                        context.getString(R.string.invalid_phone_code)
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
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {

        }
    }

    class EnterNameUi : EnterCodeUi {
        override fun apply(
            context: Context,
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeViewModel
        ) {
            binding.root.findNavController()
                .navigate(R.id.action_enterCodeFragment_to_registrationFragment)
        }
    }
}