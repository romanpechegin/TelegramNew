package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterPhoneUiCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterPhoneNumberFragmentViewModel @Inject constructor(
    private val enterPhoneUiCommunication: EnterPhoneUiCommunication,
    private val authInteractor: AuthInteractor,
    private val resources: Resources,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var phoneNumber: String = ""

    init {
        viewModelScope.launch {
            try {
                authInteractor.observeAuthState()
                    .map {
                        when (it) {
                            is TdApi.AuthorizationStateReady -> EnterPhoneUi.WaitInputPhoneUi()
                            is TdApi.AuthorizationStateWaitCode -> EnterPhoneUi.SendCodeUi()
                            is TdApi.AuthorizationStateWaitPassword -> EnterPhoneUi.WaitInputPhoneUi()
                            is TdApi.AuthorizationStateWaitPhoneNumber -> EnterPhoneUi.WaitInputPhoneUi()
                            is TdApi.AuthorizationStateWaitTdlibParameters -> EnterPhoneUi.WaitInputPhoneUi()
                            is TdApi.AuthorizationStateWaitEncryptionKey -> EnterPhoneUi.WaitInputPhoneUi()
                            else -> EnterPhoneUi.ErrorPhoneUi(Throwable(it.javaClass.simpleName))
                        }
                    }
                    .catch { e ->
                        enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                    }
                    .collectLatest {
                        enterPhoneUiCommunication.map(it)
                    }

            } catch (e: Exception) {
                enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
            }
        }
    }

    private fun correctPhoneNumber(phoneNumber: String = this.phoneNumber): String? {
        val onlyDigit = phoneNumber.filter { it.isDigit() }
        this.phoneNumber = onlyDigit
        return if (onlyDigit.length == resources.getInt(R.integer.phone_size)) onlyDigit
        else null
    }

    fun inputPhoneNumber(phoneNumber: String) {
        if (this.phoneNumber != phoneNumber) {
            val correctPhone = correctPhoneNumber(phoneNumber)
            if (correctPhone != null) enterPhoneUiCommunication.map(EnterPhoneUi.CompleteInputPhoneUi())
            else enterPhoneUiCommunication.map(EnterPhoneUi.WaitInputPhoneUi())
        } else enterPhoneUiCommunication.map(EnterPhoneUi.WaitInputPhoneUi())
    }

    fun observeEnterPhoneUi(owner: LifecycleOwner, observer: Observer<EnterPhoneUi>) =
        enterPhoneUiCommunication.observe(owner, observer)

    fun sendCode() {
        if (correctPhoneNumber() != null) {
            viewModelScope.launch(ioDispatcher) {
                try {
                    authInteractor.checkPhoneNumber(phoneNumber)
                } catch (e: Throwable) {
                    enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                }
            }
        }
    }
}