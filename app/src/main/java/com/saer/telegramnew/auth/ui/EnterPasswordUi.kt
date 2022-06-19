package com.saer.telegramnew.auth.ui

interface EnterPasswordUi {

    fun apply()

    class WainPasswordUi : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }

    class IncorrectPasswordUi : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }

    class SuccessPasswordUi : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }
}