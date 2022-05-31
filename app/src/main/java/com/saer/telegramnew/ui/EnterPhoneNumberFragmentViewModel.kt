package com.saer.telegramnew.ui

import androidx.lifecycle.*
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.ResultCheckPhoneCommunication
import com.saer.telegramnew.communications.ResultSendCodeCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import com.saer.telegramnew.model.*
import javax.inject.Inject

class EnterPhoneNumberFragmentViewModel @Inject constructor(
    private val resultCheckPhoneCommunication: ResultCheckPhoneCommunication,
    private val resultSendCodeCommunication: ResultSendCodeCommunication,
    private val authInteractor: AuthInteractor,
    private val resources: Resources
) : ViewModel() {

    private var phoneNumber: String = ""

    fun inputPhoneNumber(phoneNumber: String) {
        val onlyNumber = phoneNumber.filter { it.isDigit() }
        this.phoneNumber = onlyNumber
        if (onlyNumber.length == resources.getInt(R.integer.phone_size)) {
            resultCheckPhoneCommunication.map(authInteractor.checkPhoneNumber(onlyNumber))
        }
        else {
            resultCheckPhoneCommunication.map(ErrorResult())
        }
    }

    fun observeCheckPhoneResult(owner: LifecycleOwner, observer: Observer<Result<Boolean>>) =
        resultCheckPhoneCommunication.observe(owner, observer)

    fun observeSendCodeResult(owner: LifecycleOwner, observer: Observer<Result<Boolean>>) =
        resultSendCodeCommunication.observe(owner, observer)

    fun sendCode() {
        when (resultCheckPhoneCommunication.value) {
            is ErrorResult -> resultSendCodeCommunication.map(ErrorResult())
            is SuccessResult -> resultSendCodeCommunication.map(SuccessResult())
            is PendingResult -> resultSendCodeCommunication.map(PendingResult())
            null -> throw Exception() // TODO
        }
    }
}