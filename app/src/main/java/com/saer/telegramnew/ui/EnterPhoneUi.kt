package com.saer.telegramnew.ui

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import com.saer.telegramnew.R

interface EnterPhoneUi {

    fun apply(sendCodeButton: Button, phoneTitle: TextView, context: Context, navController: NavController)

    class WaitInputPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: Button,
            phoneTitle: TextView,
            context: Context,
            navController: NavController
        ) {
            sendCodeButton.visibility = View.GONE
            phoneTitle.text = context.getString(R.string.enter_phone_number)
        }
    }

    class SuccessPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: Button,
            phoneTitle: TextView,
            context: Context,
            navController: NavController
        ) {
            navController.navigate(R.id.action_EnterPhoneNumberFragment_to_enterCodeFragment)
        }
    }

    class CompleteInputPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: Button,
            phoneTitle: TextView,
            context: Context,
            navController: NavController
        ) {
            sendCodeButton.visibility = View.VISIBLE
            phoneTitle.text = context.getString(R.string.click_send_code)
        }
    }

    class ErrorPhoneUi : EnterPhoneUi {
        override fun apply(
            sendCodeButton: Button,
            phoneTitle: TextView,
            context: Context,
            navController: NavController
        ) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}