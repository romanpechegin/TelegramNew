package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.login.R
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterPhoneNumberViewModel(
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
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: EnterPhoneUi) -> Unit
    ) = enterPhoneUiCommunication.observe(
        viewLifecycleOwner = viewLifecycleOwner,
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

    @Suppress("UNCHECKED_CAST")
    @LoginFeature
    class Factory @Inject constructor(
        private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
        private val authRepository: AuthRepository,
        private val resources: Resources,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EnterPhoneNumberViewModel::class.java)
            return EnterPhoneNumberViewModel(
                enterPhoneUiCommunication,
                authRepository,
                resources,
                ioDispatcher
            ) as T
        }
    }
}