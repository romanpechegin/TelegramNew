package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.telegramnew.R
import com.saer.telegramnew.auth.repositories.AuthRepository
import com.saer.telegramnew.common.Communication
import com.saer.telegramnew.common.Resources
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterPhoneNumberViewModel @Inject constructor(
    private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
    private val authRepository: AuthRepository,
    private val resources: Resources,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var phoneNumber: String = ""

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitCode -> EnterPhoneUi.SendCodeUi()
                        is TdApi.AuthorizationStateWaitPassword -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterPhoneUi.WaitEnterPhoneUi()
                        else -> EnterPhoneUi.ErrorPhoneUi(Throwable(it.javaClass.simpleName))
                    }
                }
                .catch { e ->
                    enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                }
                .collectLatest {
                    enterPhoneUiCommunication.map(it)
                }
        }
    }

    private fun correctPhoneNumber(phoneNumber: String = this.phoneNumber): String? {
        val onlyDigit = phoneNumber.filter { it.isDigit() }
        this.phoneNumber = onlyDigit
        return if (onlyDigit.length == resources.getInt(R.integer.phone_size)) onlyDigit
        else null
    }

    fun enterPhoneNumber(phoneNumber: String) {
        if (this.phoneNumber != phoneNumber) {
            val correctPhone = correctPhoneNumber(phoneNumber)
            if (correctPhone != null) enterPhoneUiCommunication.map(EnterPhoneUi.CompleteEnterPhoneUi())
            else enterPhoneUiCommunication.map(EnterPhoneUi.WaitEnterPhoneUi())
        } else enterPhoneUiCommunication.map(EnterPhoneUi.WaitEnterPhoneUi())
    }

    fun observeEnterPhoneUi(
        lifecycleCoroutineScope: LifecycleCoroutineScope,
        collector: FlowCollector<EnterPhoneUi>
    ) =
        enterPhoneUiCommunication.observe(
            lifecycleCoroutineScope = lifecycleCoroutineScope,
            collector = collector
        )

    fun sendCode() {
        if (correctPhoneNumber() != null) {
            viewModelScope.launch(ioDispatcher) {
                try {
                    authRepository.checkPhoneNumber(phoneNumber)
                } catch (e: Throwable) {
                    enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                }
            }
        }
    }
}