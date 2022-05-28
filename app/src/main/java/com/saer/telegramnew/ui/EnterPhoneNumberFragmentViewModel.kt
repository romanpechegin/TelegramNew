package com.saer.telegramnew.ui

import androidx.lifecycle.*
import com.saer.telegramnew.R
import com.saer.telegramnew.communication.ResultCommunication
import com.saer.telegramnew.model.*
import javax.inject.Inject

class EnterPhoneNumberFragmentViewModel @Inject constructor(
    private val resultCommunication: ResultCommunication
) : ViewModel() {

    fun inputPhoneNumber(phoneNumber: String) {
        val onlyNumber = phoneNumber.filter { it.isDigit() }
        if (onlyNumber.length == 11) resultCommunication.map(UserActionSuccessResult(R.string.click_send_code))
        else resultCommunication.map(UserActionErrorResult(R.string.enter_phone_number))
    }

    fun observeResult(owner: LifecycleOwner, observer: Observer<Result<Any>>) =
        resultCommunication.observe(owner, observer)
}