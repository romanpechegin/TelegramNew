package com.saer.login.ui

import androidx.navigation.findNavController
import com.saer.login.databinding.FragmentEnterPasswordBinding
import com.saer.navigation.R

interface EnterPasswordUi {

    fun apply(binding: FragmentEnterPasswordBinding)

    class Wait : EnterPasswordUi {
        override fun apply(binding: FragmentEnterPasswordBinding) {
            TODO("Not yet implemented")
        }
    }

    class IncorrectPassword : EnterPasswordUi {
        override fun apply(binding: FragmentEnterPasswordBinding) {
            TODO("Not yet implemented")
        }
    }

    class Success : EnterPasswordUi {
        override fun apply(binding: FragmentEnterPasswordBinding) {
            binding.applyButton.findNavController().navigate(R.id.action_global_chats_graph)
        }
    }

    class ErrorUi(private val throwable: Throwable) : EnterPasswordUi {
        override fun apply(binding: FragmentEnterPasswordBinding) {
            throwable.printStackTrace()
        }
    }

    class Registration : EnterPasswordUi {
        override fun apply(binding: FragmentEnterPasswordBinding) {

        }
    }
}