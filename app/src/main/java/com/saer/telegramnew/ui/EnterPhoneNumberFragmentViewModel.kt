package com.saer.telegramnew.ui

import androidx.lifecycle.*
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterPhoneUiCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterPhoneNumberFragmentViewModel @Inject constructor(
    private val enterPhoneUiCommunication: EnterPhoneUiCommunication,
    private val authInteractor: AuthInteractor,
    private val resources: Resources
) : ViewModel() {

    private var phoneNumber: String = ""

    init {
        viewModelScope.launch {
            authInteractor.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> TODO()
                        is TdApi.AuthorizationStateWaitCode -> EnterPhoneUi.SuccessPhoneUi()
                        is TdApi.AuthorizationStateWaitPassword -> TODO()
                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterPhoneUi.WaitInputPhoneUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> null
                        is TdApi.AuthorizationStateWaitEncryptionKey -> null
                        else -> EnterPhoneUi.ErrorPhoneUi()
                    }
                }
                .collectLatest {
                    if (it != null) {
                        enterPhoneUiCommunication.map(it)
                    }
                }
        }
    }

    fun inputPhoneNumber(phoneNumber: String) {
        val onlyNumber = phoneNumber.filter { it.isDigit() }
        if (this.phoneNumber != onlyNumber) {
            this.phoneNumber = onlyNumber
            if (onlyNumber.length == resources.getInt(R.integer.phone_size)) {
                enterPhoneUiCommunication.map(EnterPhoneUi.CompleteInputPhoneUi())
            } else {
                enterPhoneUiCommunication.map(EnterPhoneUi.WaitInputPhoneUi())
            }
        }
    }

    fun observeEnterPhoneUi(owner: LifecycleOwner, observer: Observer<EnterPhoneUi>) =
        enterPhoneUiCommunication.observe(owner, observer)

    fun sendCode() {
        when (enterPhoneUiCommunication.value) {
            is EnterPhoneUi.CompleteInputPhoneUi -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authInteractor.checkPhoneNumber(phoneNumber)
                }
            }
            else -> throw IllegalStateException()
        }
    }
}