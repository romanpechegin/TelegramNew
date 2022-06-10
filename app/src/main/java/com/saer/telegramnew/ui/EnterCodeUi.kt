package com.saer.telegramnew.ui

import android.util.Log
import com.saer.telegramnew.databinding.FragmentEnterCodeBinding

interface EnterCodeUi {
    fun apply(binding: FragmentEnterCodeBinding, viewModel: EnterCodeFragmentViewModel)

    class WaitCodeUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {
            binding.enterCodeEditText.isEnabled = true
        }

    }

    class ErrorCodeFormatUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {

        }

    }

    class WaitPasswordUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {
            Log.e("TAG", "apply: ${javaClass.simpleName}")
        }
    }

    class CompleteEnterCodeUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {
            binding.enterCodeEditText.isEnabled = false
        }
    }

    class SuccessAuthUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {

        }

    }

    class ErrorCodeUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {
            Log.e("TAG", "apply: ${javaClass.simpleName}")
        }

    }

    class WaitPhoneUi : EnterCodeUi {
        override fun apply(
            binding: FragmentEnterCodeBinding,
            viewModel: EnterCodeFragmentViewModel
        ) {

        }

    }
}