package com.saer.login.ui

interface EnterPasswordUi {

    fun apply()

    class Wait : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }

    class IncorrectPassword : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }

    class Success : EnterPasswordUi {
        override fun apply() {
            TODO("Not yet implemented")
        }
    }

    class ErrorUi(private val throwable: Throwable) : EnterPasswordUi {
        override fun apply() {
            throwable.printStackTrace()
        }
    }

    class Registration : EnterPasswordUi {
        override fun apply() {

        }
    }
}